package utilities.yamlReader;
import org.yaml.snakeyaml.Yaml;
import utilities.Logger.LoggingUtils;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Random;
public class yamlReader {
    /** The YAML file name. */
    private String yamlFileName;

    /** The YAML object. */
    private Map<String, Object> yamlData;
    private String filePath;

    public yamlReader(){
        this.filePath = ".\\src\\test\\java\\resources\\testData.yaml";
        this.yamlFileName = this.filePath;
        yamlLoader();
    }

    private void yamlLoader(){
        try{
            Yaml yaml = new Yaml();
            FileInputStream fileInputStream = new FileInputStream(yamlFileName);
            yamlData = yaml.load(fileInputStream);
        }catch (FileNotFoundException e){
            LoggingUtils.error(e.getMessage());
        }
    }

    public Map<String, Object> getRoleData(String role) {
        List<Map<String, Object>> roles = (List<Map<String, Object>>) yamlData.get("Roles");
        for (Map<String, Object> r : roles) {
            String rRole = (String) r.get("role");
            if (role.equals(rRole)) {
                return r;
            }
        }
        return null;
    }

    public String getEmailByRole(String role){
        Map<String, Object> roleData = getRoleData(role);
        if (roleData != null) {
            return (String) roleData.get("gmail_email");
        }
        return null;
    }
    public String getPasswordByRole(String role){
        Map<String, Object> roleData = getRoleData(role);
        if (roleData != null) {
            return (String) roleData.get("gmail_password");
        }
        return null;
    }

    public String getKpxUsername(String role){
        Map<String, Object> roleData = getRoleData(role);
        if (roleData != null) {
            return roleData.get("kpx_username").toString();
        }
        return null;
    }

    public String getKpxPassword(String role){
        Map<String, Object> roleData = getRoleData(role);
        if (roleData != null) {
            return roleData.get("kpx_password").toString();
        }
        return null;
    }

    public String getAccessKey() {
        return (String) yamlData.get("AccessKey");
    }
    public String getRandomUser(){
        String user = "Users";
            try {
            @SuppressWarnings("unchecked")
            List<String> randomUser = (List<String>) yamlData.get(user);
            if (randomUser == null || randomUser.isEmpty()) {
                LoggingUtils.error("Tag not available");
                return null;
            }
            int randomIndex = new Random().nextInt(randomUser.size());
            String randomTagValue = randomUser.get(randomIndex);
            // randomUser.remove(randomIndex);   
            return randomTagValue;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
