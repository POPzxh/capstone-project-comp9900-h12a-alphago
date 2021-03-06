package alphago.propertysale.utils;

import alphago.propertysale.entity.AvatarPorter;
import alphago.propertysale.entity.ImgPorter;
import org.springframework.util.ClassUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import java.util.UUID;

/**
 * @program: image_server
 * @description:
 * @author: XIAO HAN
 * @create: 2020-10-09 17:34
 **/
public class ImageUtil {
//    private static final String PATH = ClassUtils.getDefaultClassLoader().getResource("static").getPath();
    public static final String PATH = System.getProperty("user.dir");
    private static final String IMG = PATH + "/img/";

    public static void AvatarSave(AvatarPorter porter){
        byte[] bytes = porter.getAvatar();;
        long uid = porter.getUid();
        String name = porter.getName();
        String type = getType(name);

        String path = IMG  +uid + "/";
        if(!isDirectoryExist(path)) new File(path).mkdir();
        path += "avatar/";
        if(!isDirectoryExist(path)) new File(path).mkdir();
        File parent = new File(path);
        // delete exist old file
        File[] files = parent.listFiles();
        assert files != null;
        Arrays.stream(files).forEach(File::delete);
         //save avatar
        path += "avatar" + type;
        File file = new File(path);
        try(FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedOutputStream outputStream = new BufferedOutputStream(fileOutputStream)) {
            outputStream.write(bytes);
        }catch (IOException e){
            System.out.println("fail");
        }
    }

    public static void saveImage(ImgPorter imgPorter){
        long pid = imgPorter.getPid();
        String type = getType(imgPorter.getName());
        String name = UUID.randomUUID() + type;

        String path = IMG  + "property" + "/";
        if(!isDirectoryExist(path)) new File(path).mkdir();
        path += pid + "/";
        if(!isDirectoryExist(path)) new File(path).mkdir();
        path += name;
        File file = new File(path);
        try(FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedOutputStream outputStream = new BufferedOutputStream(fileOutputStream)) {
            outputStream.write(imgPorter.getImage());
        }catch (IOException e){
            System.out.println("fail");
        }
    }

    public static String[] getCover(int pid){
        String path = IMG + "property/" + pid;
        File directory = new File(path);
        return directory.list();
    }

    public static boolean isDirectoryExist(String path){
        File file = new File(path);
        return file.exists();
    }

    public static void remove(String pid){
        String path = IMG + "property/" + pid;
        File directory = new File(path);
        for(File file : directory.listFiles()){
            file.delete();
        }
        directory.delete();
    }

    public static String getType(String fileName){
        return fileName.substring(fileName.lastIndexOf('.'));
    }
}