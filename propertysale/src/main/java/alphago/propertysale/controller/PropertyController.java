package alphago.propertysale.controller;

import alphago.propertysale.entity.Address;
import alphago.propertysale.entity.ImgPorter;
import alphago.propertysale.entity.Property;
import alphago.propertysale.entity.returnVO.PropertyVO;
import alphago.propertysale.rabbit.MessageProducer;
import alphago.propertysale.service.AddressService;
import alphago.propertysale.service.PropertyService;
import alphago.propertysale.shiro.JwtInfo;
import alphago.propertysale.utils.CheckCode;
import alphago.propertysale.utils.FileUtil;
import alphago.propertysale.utils.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: propertysale
 * @description: Controller for Property
 * @author: XIAO HAN
 * @create: 2020-10-13 15:34
 **/
@RestController
@RequestMapping("/property")
public class PropertyController {
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private MessageProducer messageProducer;
    @Value("${remote.url}")
    private String url;
    // property registration
    @RequestMapping("/registration")
    @RequiresAuthentication
    public Result propertyRegister(Property property, Address address, MultipartFile[] photos) throws IOException {
        // get owner's id
        Subject subject = SecurityUtils.getSubject();
        JwtInfo info = (JwtInfo) subject.getPrincipal();
        long ownerId = info.getUid();

        // Register Property
        property.setOwner(ownerId);
        propertyService.save(property);
        // Save Address
        Long pid = property.getPid();
        address.setPid(pid);
        addressService.save(address);

        // upload file
        ImgPorter[] imgPorters = new ImgPorter[photos.length];
        for(int i = 0 ; i < photos.length ; i++){
            imgPorters[i] = new ImgPorter().setImage(photos[i].getBytes())
                    .setName(photos[i].getOriginalFilename())
                    .setPid(pid).setUid(ownerId);
        }
        messageProducer.sendMsg(imgPorters , CheckCode.IMAGE);
        return Result.success("success");
    }

    @RequestMapping("/information")
    public Result information(){
        Subject subject = SecurityUtils.getSubject();
        JwtInfo info = (JwtInfo) subject.getPrincipal();
        long owner = info.getUid();
        List<Property> properties = propertyService.list(new QueryWrapper<Property>().
                eq("owner", owner).select("pid", "bathroom_num", "bedroom_num", "garage_num", "type", "area"));

        List<PropertyVO> voList = new ArrayList<>();
        for(Property property : properties){
            Address address = addressService.getById(property.getPid());
            voList.add(new PropertyVO().setId(property.getPid())
                                    .setPhoto(FileUtil.getImages(owner , property.getPid()))
                                    .setAddress(address.getFullAddress())
                                    .setArea(property.getArea())
                                    .setBathroomNum(property.getBathroomNum())
                                    .setBedroomNum(property.getBedroomNum())
                                    .setGarageNum(property.getGarageNum())
                                    .setType(property.getType()));
        }
        return Result.success(voList);
    }
}