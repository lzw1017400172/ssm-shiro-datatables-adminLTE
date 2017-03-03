package com.kaishengit.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kaishengit.dto.DeviceRentDto;
import com.kaishengit.dto.wx.Message;
import com.kaishengit.exception.NotFoundException;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.mapper.*;
import com.kaishengit.pojo.*;
import com.kaishengit.service.DeviceService;
import com.kaishengit.service.WeiXinService;
import com.kaishengit.utiils.SerialNumberUtils;
import com.kaishengit.utiils.ShiroUtils;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by 刘忠伟 on 2017/1/17.
 */
/*纳入bean管理,不管理接口*/
@Service
public class DeviceServiceImpl implements DeviceService{

    Logger logger = LoggerFactory.getLogger(DeviceServiceImpl.class);
    /*扫描过DeviceMapper接口之后，就把实现类对象放入spring容器了。只需要自动注入。*/
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private DeviceRentMapper deviceRentMapper;
    @Autowired
    private DeviceRentDetailMapper deviceRentDetailMapper;
    /*mapper接口，在mybatis配置中经过扫描，自动创建实现类及其对象，可以自动注入*/
    @Autowired
    private DeviceRentDocsMapper deviceRentDocsMapper;
    @Autowired
    private FinanceMapper financeMapper;
    @Autowired
    private WeiXinService weiXinService;

    @Value("${upload.path}")//在类中自动注入config文件
    private String filePath;

    @Override
    public void save(Device device) {
        //一般在添加数据的时候不直接使用对象传值，可以使用另一种传值方式map
        Map<String,Object> map = Maps.newHashMap();
        map.put("name",device.getName());
        map.put("unit",device.getUnit());
        map.put("totalNum",device.getTotalNum());
        map.put("price",device.getPrice());

        deviceMapper.save(map);

    }

    /**
     *
     * @return 返回所有device设备信息
     */
    @Override
    public List<Device> findAll() {
        return deviceMapper.findAll();
    }

    @Override
    public Device findById(Integer id) {
        Device device = deviceMapper.findById(id);
/*        if(device == null){
            throw new NotFoundException();//使用spring框架自定义的运行时异常，专门针对找不到
        }*/
        return device;
    }

    /**
     * 修改设备信息
     * @param device
     */
    @Override
    public void update(Device device) {

        //为了保险，首先先查询一下存在不存在
        if(deviceMapper.findById(device.getId()) == null){
            throw new NotFoundException();//找不到，抛异常
        }


        deviceMapper.update(device);

    }

    /**
     *  datatables插件的ajax查询，分页。不需要page对象了
     * @param
     * @param
     * @return
     */
    @Override
    public List<Device> findPageDataTables(Map<String,Object> map) {
        List<Device> deviceList = deviceMapper.findAllPageDaTables(map);
        return deviceList;
    }

    @Override
    public Long findCount(String deviceName) {
        return deviceMapper.count(deviceName);
    }

    @Override
    public void delete(Integer id) {
        deviceMapper.del(id);
    }

    /**
     * 添加设备的租赁记录,需要把数据存到三张表，t_device_rent,t_device_rent_detail,t_device_rent_docs。设备表不能和这个租赁用一个，租赁表示记录
     * 并且只要添加租赁记录，就要添加财务记录，付预付款。财务表
     *
     * 分别存入三张表，需要开启事务，如果只有一个表改变，不需要开启事务。一个完成或者失败就是全部完成或者失败
     * @param deviceRentDto
     */
    @Override
    @Transactional
    public String saveRent(DeviceRentDto deviceRentDto) throws RuntimeException{

        //1保存租赁合同
        DeviceRent rent = new DeviceRent();
        rent.setCompanyName(deviceRentDto.getCompanyName());
        rent.setLinkMan(deviceRentDto.getLinkMan());
        rent.setCardNum(deviceRentDto.getCardNum());
        rent.setTel(deviceRentDto.getTel());
        rent.setAddress(deviceRentDto.getAddress());
        rent.setFax(deviceRentDto.getFax());
        rent.setRentDate(deviceRentDto.getRentDate());
        rent.setBackDate(deviceRentDto.getBackDate());
        rent.setTotalDay(deviceRentDto.getTotalDay());
        //总费用和尾款，直接在服务端计算，更加严谨
        rent.setTotalPrice(0f);     //总费用=每个设备的总费用相加。如果在这里计算，需要对DeviceRentDto中deviceArray进行迭代，计算每个总数再相加
        rent.setPreCost(0f);        //或者，在下面记录device_rent_detail表时，算出来每个设备的总费用再相加，通过修改表来更新此表。
        rent.setLastCost(0f);
        rent.setCreateTime(DateTime.now().toString("YYYY-MM-DD HH:mm:ss"));
        rent.setCreateUser(ShiroUtils.getUserName());//当前登录的用户使用shiro登录认证获取传入session的值，可以在客户端和服务端获取
        rent.setSerialNumber(SerialNumberUtils.getSerialNumber());//合同的序列号，需要生成 当前日期+后四位数字随机
        rent.setStatus("未完成");
        //在写入数据库时使用返回id的，因为在存入下面两张表时需要租赁合同id
        deviceRentMapper.save(rent);//返回值id自动保存到rent对象

        //2保存合同详情(一个合同对应多个设备信息，一对多，都用这一个id)
        //需要把DeviceRentDto里面的DeviceArray集合，转换成DeviceRentDetail集合，需要迭代
        List<DeviceRentDto.DeviceArrayBean> beanList = deviceRentDto.getDeviceArray();
        List<DeviceRentDetail> detailList = new ArrayList<DeviceRentDetail>();
        //声明一个变量为设备总费用
        float totalCost = 0;
        //迭代空集合无所谓不会报错
        for (DeviceRentDto.DeviceArrayBean bean : beanList) {
            //每循环一次，创建一个DeviceRentDetail对象，把DeviceRentDto.DeviceArrayBean转化成创建一个DeviceRentDetail对象装入detailList
            DeviceRentDetail deviceRentDetail = new DeviceRentDetail();
            deviceRentDetail.setDeviceName(bean.getDeviceName());
            deviceRentDetail.setDeviceUnit(bean.getUnit());
            deviceRentDetail.setDevicePrice(bean.getRentPrice());
            deviceRentDetail.setDeviceNum(bean.getRentNum());//租赁的数量，需要同步到device设备表的库存
            deviceRentDetail.setTotalPrice(bean.getTotalPrice());//当前设备的总费用
            deviceRentDetail.setRentId(rent.getId());



            //正好上面添加合同信息时，没有添加总费用和预付款，现在借用这个循环计算总费用，使用修改的方式改变
            totalCost += bean.getTotalPrice();

            //此集合代表所有租赁的设备，更新设备表的数量，每个设备都更新  库存= 库存-bean.getRentNum()
            //更新device库存时，可能会出现负数，尽管客户端验证不会大于库存，但是多线程，会出现别人已经操作过的情况，
            //所以这里再查一次库存，进行判断，才不会出现负数
            Device device = deviceMapper.findById(bean.getId());

            if(device.getCurrNum() < bean.getRentNum()){//小于的话会回滚
                throw new ServiceException(bean.getDeviceName()+"库存不足");
            }else {
                device.setCurrNum(device.getCurrNum() - bean.getRentNum());
                //修改设备device
                deviceMapper.update(device);
            }
            detailList.add(deviceRentDetail);
        }
        //批量添加数据，此集合一定不能为空，在客户端使用表单验证来限制，服务端也要使用不为null
        if(!detailList.isEmpty()){
            deviceRentDetailMapper.saves(detailList);
            //修改合同的总费用及其预付款
            deviceRentMapper.updateCost(totalCost,totalCost*0.3,totalCost-totalCost*0.3,rent.getId());
            rent.setPreCost((float) (totalCost*0.3));
        } else {
            //抛出异常，回滚
            throw new RuntimeException("没有添加设备信息");
        }



        //3保存文件
        List<DeviceRentDto.FileArrayBean> fileArrayBeanList = deviceRentDto.getFileArray();
        List<DeviceRentDocs> deviceRentDocsList = Lists.newArrayList();
        //依然使用迭代(迭代空不会报错)，将fileArrayBeanList的对象，转化到DeviceRentDocs集合，才能插入表
        for(DeviceRentDto.FileArrayBean bean:fileArrayBeanList){
            DeviceRentDocs deviceRentDocs = new DeviceRentDocs();
            deviceRentDocs.setSourceName(bean.getSourceFileName());
            deviceRentDocs.setNewName(bean.getNewFileName());
            deviceRentDocs.setRentId(rent.getId());
            deviceRentDocsList.add(deviceRentDocs);
        }
        //若果deviceRentDocsList为空则不保存，保存会sql错误
        if(!deviceRentDocsList.isEmpty()){
            //一个合同对应多个文件，一对多，可以批量添加
            deviceRentDocsMapper.saves(deviceRentDocsList);
        } else {
            throw new RuntimeException("没有添加合同文件");
        }


        //4写入财务流水，支付预付款
        Finance finance = new Finance();
        finance.setType("收入");
        finance.setState("未确认");
        finance.setSerialNumber(SerialNumberUtils.getSerialNumber());
        finance.setModule("设备租赁");
        finance.setMoney( rent.getPreCost());

        finance.setCreateUser(ShiroUtils.getUserName());//当前设备租赁登录的人
        finance.setRemark("设备租赁预付款");//备注
        finance.setModuleSerialNumber(rent.getSerialNumber());//模块的序列号
        //因为未确认，所以确认日期和确认人为nulll。确认是有财务部操作人员收到预付款后点击确认

        financeMapper.save(finance);

        //把流水号返回到jsp，可以通过流水号查找显示内容
        return rent.getSerialNumber();
    }

    /**
     * 根据流水号查询合同，先查deviceRent,再查合同详情detail,后插文件docs
     *
     * @param serialNumber
     * @return
     */
    @Override
    public Map<String, Object> findBySerialNumber(String serialNumber) {
        //查合同表
        DeviceRent deviceRent = deviceRentMapper.findBySerialNumber(serialNumber);
        //根据合同id查详情
        List<DeviceRentDetail> deviceRentDetails = deviceRentDetailMapper.findByRentId(deviceRent.getId());
        //根据合同id查文件
        List<DeviceRentDocs> deviceRentDocses = deviceRentDocsMapper.findByRentId(deviceRent.getId());

        Map<String,Object> map = Maps.newHashMap();
        map.put("deviceRent",deviceRent);
        map.put("deviceRentDetails",deviceRentDetails);
        map.put("deviceRentDocses",deviceRentDocses);

        return map;
    }

    /**
     * 设备租赁合同文件下载单个
     * @param id
     *
     */
    @Override
    public FileInputStream download(Integer id) throws IOException {
        //根据文件id查文件

        DeviceRentDocs docs = deviceRentDocsMapper.findBytId(id);
        if(docs != null){
            FileInputStream inputStream = new FileInputStream(new File(filePath+"/"+docs.getNewName()));
            return inputStream;
        }else{
            return null;
        }
        //需要文件新名字为路径


    }

    /**
     * 根据文件id查询文件
     * @param id
     * @return
     */
    @Override
    public DeviceRentDocs findDocsById(Integer id) {
        return deviceRentDocsMapper.findBytId(id);
    }

    /**
     * 根据id查合同
     * @param id
     * @return
     */
    @Override
    public DeviceRent findRentById(Integer id) {
        return deviceRentMapper.findById(id);
    }

    /**
     * 文件打包成zip包下载。
     * @param rent
     * @param zipOutputStream
     */
    @Override
    public void zipDownload(DeviceRent rent, ZipOutputStream zipOutputStream) throws IOException {

        List<DeviceRentDocs> docsList = deviceRentDocsMapper.findByRentId(rent.getId());

        //每个文件都要有一个输入流,对应创建多少个项entity,对应塞进zip输出流
        for(DeviceRentDocs docs:docsList){
            ZipEntry entry = new ZipEntry(docs.getSourceName());//zip包的很多项
            zipOutputStream.putNextEntry(entry);//把项都加入zip包

            //创建每个文件的输入流
            FileInputStream inputStream = download(docs.getId());
            IOUtils.copy(inputStream,zipOutputStream);
            //关闭每一个文件输入流
            inputStream.close();
        }

        //最后关闭输出流
        zipOutputStream.closeEntry();
        zipOutputStream.flush();
        zipOutputStream.close();



    }

    /**
     * 查询deviceRent的全部，分页，默认id排序
     * @param start
     * @param length
     * @return
     */
    @Override
    public List<DeviceRent> findDeviceRent(Integer start, Integer length) {
        return deviceRentMapper.findAll(start,length);
    }

    /**
     * 查询有多少数据
     * @return
     */
    @Override
    public Integer findDeviceRentCount() {
        return deviceRentMapper.findCount();
    }

    /**
     * 把设备租赁状态变成 完成。1 需要设备入库，2设备库存数量增加  3支付尾款录入财务   必须开启事务
     * 并且接入微信接口，给微信里的财务部人员发送消息去收尾款。微信里面的财务部id对应数据库的财务部id
     * @param id
     */
    @Override
    @Transactional
    public void finish(Integer id) {
        DeviceRent deviceRent = deviceRentMapper.findById(id);
        if(deviceRent != null){
            //为了严谨，需要查一遍数据，是否状态已经改变，别的人操作了
            if("未完成".equals(deviceRent.getStatus())){//java不能双等号比较字符串，双等号比较内存地址，肯定不同
                //变成完成状态
                deviceRent.setStatus("完成");
                deviceRentMapper.update(deviceRent);

                //设备入库.设备库存数量增加
                //先把deviceRent的id当作rent_id去查询rent_datail表。查出来租设备的信息，
                //然后迭代，根据设备名字去查设备表，库存数加上租的个数
                List<DeviceRentDetail> detailList = deviceRentDetailMapper.findByRentId(deviceRent.getId());
                for(DeviceRentDetail detail:detailList){
                    Device device = deviceMapper.findByName(detail.getDeviceName());
                    device.setCurrNum(device.getCurrNum()+detail.getDeviceNum());
                    deviceMapper.update(device);
                }


                //支付尾款，录入财务。创建一条财务尾款，但是未确认，需要财务部收到钱再改成确认。已付款
                Finance finance = new Finance();
                finance.setType("收入");
                finance.setState("未确认");
                finance.setSerialNumber(SerialNumberUtils.getSerialNumber());
                finance.setModule("设备租赁");
                finance.setMoney(deviceRent.getLastCost());

                finance.setCreateUser(ShiroUtils.getUserName());//当前设备租赁登录的人
                finance.setRemark("设备租赁尾款");//备注
                finance.setModuleSerialNumber(deviceRent.getSerialNumber());//模块的流水号号
                //因为未确认，所以确认日期和确认人为nulll。确认是有财务部操作人员收到预付款后点击确认

                financeMapper.save(finance);

                //接入微信接口，给财务部发消息。
                Message message = new Message();
                message.setToparty("6");//发多个部门使用|分隔
                message.setAgentid(0);
                message.setMsgtype("text");
                message.setSafe(0);
                Message.TextBean bean = new Message.TextBean();
                bean.setContent("设备租赁尾款:"+deviceRent.getLastCost()+"元，请收款。流水号："+finance.getSerialNumber());
                message.setText(bean);
                weiXinService.wxSendMessage(message);

            } else {
                throw new ServiceException("您要需要的合同已经处于'完成'状态，请刷新后再试！");
            }
        } else {
            throw new NotFoundException();
        }

    }


}
