package com.kaishengit.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kaishengit.dto.OutSourcingDto;
import com.kaishengit.exception.NotFoundException;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.mapper.*;
import com.kaishengit.pojo.*;
import com.kaishengit.service.OutSourcingService;
import com.kaishengit.utiils.Page;
import com.kaishengit.utiils.SerialNumberUtils;
import com.kaishengit.utiils.ShiroUtils;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by 刘忠伟 on 2017/2/18.
 */
@Service
public class OutSourcingServiceImpl implements OutSourcingService {

    /*mapper被扫描可以注入*/
    @Autowired
    private CraftMapper craftMapper;
    @Autowired
    private CraftOutSourcingMapper craftOutSourcingMapper;
    @Autowired
    private CraftOutSourcingDetailMapper craftOutSourcingDetailMapper;
    @Autowired
    private CraftOutSourcingDocsMapper craftOutSourcingDocsMapper;
    @Value("${upload.path}")
    private String filePath;
    @Autowired
    private FinanceMapper financeMapper;



    @Override
    public List<Craft> findCraftAll() {


        return craftMapper.findAll();
    }

    @Override
    public Craft findCraftById(Integer carftId) {
        return craftMapper.findById(carftId);
    }

    /**
     * 保存合同到数据库
     * @param outSourcingDto
     * @return
     */
    @Override
    @Transactional//事务。只要代码中出现异常就回滚，默认运行时异常，即使手动抛出异常也回滚。不抛出异常不会滚
    public String saveOutSourcingContract(OutSourcingDto outSourcingDto) throws RuntimeException{
        //1保存合同
        CraftOutSourcing sourcing = new CraftOutSourcing();
        sourcing.setSerialNumber(SerialNumberUtils.getSerialNumber());//流水号是需要生成
        sourcing.setCompanyName(outSourcingDto.getCompanyName());
        sourcing.setCompanyAddress(outSourcingDto.getAddress());
        sourcing.setCompanyPhone(outSourcingDto.getCompanyPhone());
        sourcing.setLinkMan(outSourcingDto.getLinkMan());
        sourcing.setLinkManPhone(outSourcingDto.getLinkManPhone());
        sourcing.setIdNum(outSourcingDto.getIdNum());
        sourcing.setCreateTime(DateTime.now().toString("YYYY-MM-DD HH:mm:ss"));
        sourcing.setCreateUser(ShiroUtils.getUserName());
        //这三个款在服务端计算，先存进去为0，因为需要循环craftArray才能算出了。之后修改即可
        sourcing.setTotalCost(0f);
        sourcing.setPreCost(0f);
        sourcing.setLastCost(0f);
        sourcing.setStatus("未完成");

        //保存数据之后，会自动将id存入此对象sourcing
        craftOutSourcingMapper.save(sourcing);

        //2保存合同详情
        List<OutSourcingDto.CarftArrayBean> beanList = outSourcingDto.getCarftArray();
        List<CraftOutSourcingDetail> detailList = Lists.newArrayList();
        Float totalCost = 0f;
        for(OutSourcingDto.CarftArrayBean bean :beanList){
            CraftOutSourcingDetail detail = new CraftOutSourcingDetail();
            detail.setCraftName(bean.getName());
            detail.setCraftPrice(bean.getPrice());
            detail.setCraftNum(bean.getOutSourcingNum());
            detail.setSubTotal(bean.getSubtotal()); //小计
            detail.setOutId(sourcing.getId());


            //因为上面没有计算总佣金，这里借助循环可以计算总佣金
            totalCost+=bean.getSubtotal();

            //还需要对工种的库存数量进行更新，每个工种都更新，借助这次迭代，进行所有工种库存更新
            //不能直接对库存更新，需要判断租赁的数量和库存大小，可能客户端验证，没有库存大，但是多线程操作，有人先租赁了，
            //会导致库存改变，所以现在再查一次库存，并且做比较。因为多线程，现在可能出现库存小于数量，需要比较
            Craft craft = craftMapper.findById(bean.getId());
            if(craft.getCurrNum()<bean.getOutSourcingNum()){
                throw new ServiceException(bean.getName()+"库存不足");
                //这里写在那里无所谓，抛出异常事务回滚
            } else {
                //保证不会出现负数，才会更新数据库
                craft.setCurrNum(craft.getCurrNum() - bean.getOutSourcingNum());
                craftMapper.update(craft);
            }

            detailList.add(detail);
        }
        //集合不能为空，为空会报错
        if(!detailList.isEmpty()) {
            craftOutSourcingDetailMapper.saves(detailList);
        } else {//当集合为空，如果不手动抛出异常，就不会有异常存在，所以事务不会回滚，上面执行这里不执行。就错了，所以使用if时一定要手动抛出异常
            throw new RuntimeException("请选择工种");     //这里抛出运行时异常，方法那里不用写也行，运行时默认上抛
        }
        //对t_craft_out的付款进行修改
        sourcing.setTotalCost(totalCost);
        sourcing.setPreCost((float) (totalCost*0.2));
        sourcing.setLastCost((float) (totalCost-totalCost*0.2));
        craftOutSourcingMapper.update(sourcing);

        //3保存文件名
        //依然需要迭代fileArray
        List<OutSourcingDto.FileArrayBean> fileBeanList = outSourcingDto.getFileArray();
        List<CraftOutSourcingDocs> docsesList = Lists.newArrayList();
        for(OutSourcingDto.FileArrayBean bean : fileBeanList){
            CraftOutSourcingDocs docs = new CraftOutSourcingDocs();
            docs.setSourceName(bean.getSourceFileName());
            docs.setNewName(bean.getNewFileName());
            docs.setOutId(sourcing.getId());

            docsesList.add(docs);
        }
        //集合为空不能保存
        if(!docsesList.isEmpty()) {
            craftOutSourcingDocsMapper.saves(docsesList);
        } else {    //这里集合为空时，必须抛出异常，因为抛出异常会让事务回滚。不抛出异常事务不会滚，就错了，上面执行这里不执行。因为这里是个if语句，不抛出异常就不会回滚
            throw new RuntimeException("请上传合同文件");
        }
        //4加入财务。只是一条未确认的财务记录
        //录入财务。创建一条财务预付款款，但是未确认，需要财务部收到钱再改成确认。已付款
        Finance finance = new Finance();
        finance.setType("收入");
        finance.setState("未确认");
        finance.setSerialNumber(SerialNumberUtils.getSerialNumber());
        finance.setModule("劳务外包");
        finance.setMoney(sourcing.getPreCost());

        finance.setCreateUser(ShiroUtils.getUserName());//当前设备租赁登录的人
        finance.setRemark("劳务外包预付款");//备注
        //因为未确认，所以确认日期和确认人为nulll。确认是有财务部操作人员收到预付款后点击确认
        finance.setModuleSerialNumber(sourcing.getSerialNumber());

        financeMapper.save(finance);

        return sourcing.getSerialNumber();
    }

    /**
     * 根据流水号查合同，三个表
     * @param serialNumber
     * @return
     */
    @Override
    public Map<String, Object> findOutSourcing(String serialNumber) {
        //先查合同表
        CraftOutSourcing craftOutSourcing = craftOutSourcingMapper.findBySerialNumber(serialNumber);
        //再查详情表

            List<CraftOutSourcingDetail> detailList = craftOutSourcingDetailMapper.findByOutId(craftOutSourcing.getId());
            //后查文件
            List<CraftOutSourcingDocs> docsList = craftOutSourcingDocsMapper.findByOutId(craftOutSourcing.getId());

        Map<String,Object> map = Maps.newHashMap();
        map.put("craftOutSourcing",craftOutSourcing);
        map.put("detailList",detailList);
        map.put("docsList",docsList);
        return map;
    }

    /**
     * 查询全部合同，不查详情，只查合同
     * @return
     */
    @Override
    public List<CraftOutSourcing> findBySearch(Integer start,Integer length,String serialNumber, String companyName, String status, String startDate, String stopDate) {
        //需要分页查
        List<CraftOutSourcing> sourcingList = craftOutSourcingMapper.findAllPage(start,length,serialNumber,companyName,status,startDate,stopDate);

        return sourcingList;
    }

    /**
     * 根据搜索一共有多少数据
     * @param serialNumber
     * @param companyName
     * @param status
     * @param startDate
     * @param stopDate
     * @return
     */
    @Override
    public Integer findBySearchCount(String serialNumber, String companyName, String status, String startDate, String stopDate) {

        return craftOutSourcingMapper.findCount(serialNumber,companyName,status,startDate,stopDate);
    }

    /**
     * 根据文件id查找文件，创建文件输入流
     * @param id
     * @return
     */
    @Override
    public FileInputStream getIntPutStream(Integer id) throws FileNotFoundException {
        CraftOutSourcingDocs docs = craftOutSourcingDocsMapper.findById(id);
        if(docs!= null){
            FileInputStream inputStream = new FileInputStream(new File(filePath+"/"+docs.getNewName()));

            return inputStream;
        } else {
            throw new NotFoundException();
        }

    }

    /**
     * 根据id查找文件
     * @param id
     * @return
     */
    @Override
    public CraftOutSourcingDocs findDocsById(Integer id) {
        return craftOutSourcingDocsMapper.findById(id);
    }

    /**
     * 根据id查合同
     * @param id
     * @return
     */
    @Override
    public CraftOutSourcing findOutSourcingById(Integer id) {
        return craftOutSourcingMapper.findById(id);
    }

    /**
     * 打包下载文件
     * @param id
     * @param zipOutputStream
     */
    @Override
    public void zipDownload(Integer id, ZipOutputStream zipOutputStream) throws IOException {
        List<CraftOutSourcingDocs> docsList = craftOutSourcingDocsMapper.findByOutId(id);
        //空集合迭代不会报错，没结果而已
        //每一个文件都需要一个文件输入流，对应拷贝进zip输出流的entity（表示zip包的每一项）
        for(CraftOutSourcingDocs docs:docsList){
            //创建zip包 里的项
            ZipEntry entry = new ZipEntry(docs.getSourceName());
            //将entry加入zipOUtstrean输出流
            zipOutputStream.putNextEntry(entry);
            //创建文件输入流,根据文件的id
            FileInputStream inputStream = getIntPutStream(docs.getId());
            //拷贝
            IOUtils.copy(inputStream,zipOutputStream);
            inputStream.close();
        }

        //关闭流
        zipOutputStream.closeEntry();
        zipOutputStream.flush();
        zipOutputStream.close();

    }

    /**
     * 修改劳务外包合同的状态为 完成
     * @param id
     */
    @Override
    @Transactional      //状态修改为完成，1 修改合同表，2 更新工种craft表的库存   3   财务流水表也要更新。所以开启事务
    public void updateStatus(Integer id) {
        //根据id查找劳务合同
        CraftOutSourcing craftOutSourcing = craftOutSourcingMapper.findById(id);
        if(craftOutSourcing != null){
            //再判断一次状态是否为    未完成 ，因为担心多线程，别人已经修改了
            if("未完成".equals(craftOutSourcing.getStatus())){
                //1修改合同状态
                    craftOutSourcing.setStatus("完成");
                    craftOutSourcingMapper.update(craftOutSourcing);


                //2更新工种库存数量
                List<CraftOutSourcingDetail> detailList = craftOutSourcingDetailMapper.findByOutId(craftOutSourcing.getId());
                for(CraftOutSourcingDetail detail:detailList){
                    Craft craft = craftMapper.findByName(detail.getCraftName());
                    craft.setCurrNum(craft.getCurrNum()+detail.getCraftNum());
                    craftMapper.update(craft);
                }


                //3更新财务流水
                //录入财务。创建一条财务尾款，但是未确认，需要财务部收到钱再改成确认。已付款
                Finance finance = new Finance();
                finance.setType("收入");
                finance.setState("未确认");
                finance.setSerialNumber(SerialNumberUtils.getSerialNumber());
                finance.setModule("劳务外包");
                finance.setMoney(craftOutSourcing.getLastCost());

                finance.setCreateUser(ShiroUtils.getUserName());//当前设备租赁登录的人
                finance.setRemark("劳务外包尾款");//备注
                finance.setModuleSerialNumber(craftOutSourcing.getSerialNumber());
                //因为未确认，所以确认日期和确认人为nulll。确认是有财务部操作人员收到预付款后点击确认

                financeMapper.save(finance);

            } else{
                throw new ServiceException("此合同处于'完成'状态，请刷新后再试！");
            }

        } else {
            throw new NotFoundException();
        }
    }
}
