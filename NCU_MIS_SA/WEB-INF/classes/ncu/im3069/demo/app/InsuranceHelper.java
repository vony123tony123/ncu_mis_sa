package ncu.im3069.demo.app;

import java.sql.*;
import org.json.*;

import ncu.im3069.demo.util.DBMgr;

// TODO: Auto-generated Javadoc
/**
 * <p>
 * The Class InsuranceHelper<br>
 * InsuranceHelper類別（class）主要管理所有與Insurance相關與資料庫之方法（method）
 * </p>
 * 
 * @author IPLab
 * @version 1.0.0
 * @since 1.0.0
 */

public class InsuranceHelper {
    
    /**
     * 實例化（Instantiates）一個新的（new）InsuranceHelper物件<br>
     * 採用Singleton不需要透過new
     */
    private InsuranceHelper() {
        
    }
    
    /** 靜態變數，儲存InsuranceHelper物件 */
    private static InsuranceHelper ih;
    
    /** 儲存JDBC資料庫連線 */
    private Connection conn = null;
    
    /** 儲存JDBC預準備之SQL指令 */
    private PreparedStatement pres = null;
    
    /**
     * 靜態方法<br>
     * 實作Singleton（單例模式），僅允許建立一個InsuranceHelper物件
     *
     * @return the helper 回傳InsuranceHelper物件
     */
    public static InsuranceHelper getHelper() {
        /** Singleton檢查是否已經有InsuranceHelper物件，若無則new一個，若有則直接回傳 */
        if(ih == null) ih = new InsuranceHelper();
        
        return ih;
    }
    
    /**
     * 透過保險編號（insurance_id）刪除保險->把delete_key從0改為1
     *
     * @param insurance_id 保險編號
     * @return the JSONObject 回傳SQL執行結果
     */
    public JSONObject delete(String insurance_id) {
        /** 紀錄回傳之資料 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "Update `missa`.`insurance` SET `delete_key` = 1 WHERE `insurance_id` = ?";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, insurance_id);
            /** 執行更新之SQL指令並記錄影響之行數 */
            row = pres.executeUpdate();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(pres, conn);
        }
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }
    
    /**
     * 取回所有保險資料
     *
     * @return the JSONObject 回傳SQL執行結果與自資料庫取回之所有資料
     */
    public JSONObject getAll() {
        /** 新建一個 Insurance 物件之 i 變數，用於紀錄每一位查詢回之保險資料 */
        Insurance i = null;
        /** 用於儲存所有檢索回之保險，以JSONArray方式儲存 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 
             * 只回傳delete_key為0者(表示未被刪除)
             * */
            String sql = "SELECT * FROM `missa`.`insurance` WHERE `delete_key` LIKE 0";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                /** 將 ResultSet 之資料取出 */
                int insurance_id = rs.getInt("insurance_id");
                String insurance_name = rs.getString("insurance_name");
                int duration_period = rs.getInt("duration_period");
                int amount_insured = rs.getInt("amount_insured");
                String details = rs.getString("details");
                int delete_key = rs.getInt("delete_key");
                Date timestamp = rs.getDate("timestamp");
                
                /** 將每一筆保險資料產生一名新Insurance物件 */
                i = new Insurance(insurance_id, insurance_name, duration_period, amount_insured, details, delete_key, timestamp);
                /** 取出該保險之資料並封裝至 JSONsonArray 內 */
                jsa.put(i.getData());
            }

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(rs, pres, conn);
        }
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }
    
    public JSONObject getByIdList(String data) {
        /** 新建一個 Insurance 物件之 i 變數，用於紀錄每一位查詢回之保險資料 */
        Insurance i = null;
        /** 用於儲存所有檢索回之保險，以JSONArray方式儲存 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;

        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            String[] in_para = DBMgr.stringToArray(data, ",");
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`insurance` WHERE `insurance`.`insurance_id`";
            for (int j=0 ; j < in_para.length ; j++) {
                sql += (j == 0) ? "in (?" : ", ?";
                sql += (j == in_para.length-1) ? ")" : "";
            }
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            for (int j=0 ; j < in_para.length ; j++) {
              pres.setString(j+1, in_para[j]);
            }
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                /** 將 ResultSet 之資料取出 */
                String insurance_name = rs.getString("insurance_name");
                int duration_period = rs.getInt("duration_period");
                int amount_insured = rs.getInt("amount_insured");
                String details = rs.getString("details");
                
                /** 將每一筆商品資料產生一個新保險物件 */
                i = new Insurance(insurance_name, duration_period, amount_insured, details);
                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                jsa.put(i.getData());
            }

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(rs, pres, conn);
        }
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間、影響行數與所有保險資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }
   
    /**
     * 請注意此方法與getByID的區別，這裡是用作會員瀏覽保險
     */
    public Insurance getByIDViewing(String id) {
        /** 新建一個 Insurance 物件之 i 變數，用於紀錄每一位查詢回之商品資料 */
        Insurance i = null;
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`insurance` WHERE `insurance`.`insurance_id` = ? LIMIT 1";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            pres.setString(1, id);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                /** 將 ResultSet 之資料取出 */
            	String insurance_name = rs.getString("insurance_name");
                int duration_period = rs.getInt("duration_period");
                int amount_insured = rs.getInt("amount_insured");
                String details = rs.getString("details");
                
                /** 將每一筆商品資料產生一名新Insurance物件 */
                i = new Insurance(insurance_name, duration_period, amount_insured, details);
            }

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(rs, pres, conn);
        }

        return i;
    }
    
    /**
     * 透過保險編號（insurance_id）取得保險資料
     *
     * @param insurance_id 保險編號
     * @return the JSON object 回傳SQL執行結果與該保險編號之保險資料
     * 
     * 為了供controller使用isEmpty檢查是否有id，這裡把id指定為string
     * 若無id傳入則回傳所有資料(getAll)，反之若有id則傳該id所屬之資料(getByID)
     * 
     * 請注意此方法與getByIDViewing的區別，此用作保險管理
     * 
     */
    
    public JSONObject getByID(String id) {
        /** 新建一個 Insurance 物件之 i 變數，用於紀錄每一位查詢回之保險資料 */
        Insurance i = null;
        /** 用於儲存所有檢索回之會員，以JSONArray方式儲存 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`insurance` WHERE `insurance_id` = ? LIMIT 1";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, id);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            /** 正確來說資料庫只會有一筆該保險編號之資料，因此其實可以不用使用 while 迴圈 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                /** 將 ResultSet 之資料取出 */
                int insurance_id = rs.getInt("insurance_id");
                String insurance_name = rs.getString("insurance_name");
                int duration_period = rs.getInt("duration_period");
                int amount_insured = rs.getInt("amount_insured");
                String details = rs.getString("details");
                int delete_key = rs.getInt("delete_key");
                Time timestamp = rs.getTime("timestamp");
                
                /** 將每一筆保險資料產生一名新Insurance物件 */
                i = new Insurance(insurance_id, insurance_name, duration_period, amount_insured, details, delete_key, timestamp);
                /** 取出該名保險之資料並封裝至 JSONsonArray 內 */
                jsa.put(i.getData());
            }
            
        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(rs, pres, conn);
        }
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間、影響行數與所有保險資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }
    
    /**
     * 建立該保險至資料庫
     *
     * @param i 一保險之Insurance物件
     * @return the JSON object 回傳SQL指令執行之結果
     */
    public JSONObject create(Insurance i) {
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "INSERT INTO `missa`.`insurance`(`insurance_name`, `duration_period`, `amount_insured`, `details`)"
                    + " VALUES(?, ?, ?, ?)";
            
            /** 取得所需之參數 */
            String insurance_name = i.getInsuranceName();
            int duration_period = i.getDurationPeriod();
            int amount_insured = i.getAmountInsured();
            String details = i.getDetails();
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, insurance_name);
            pres.setInt(2, duration_period);
            pres.setInt(3, amount_insured);
            pres.setString(4, details);
            
            /** 執行新增之SQL指令並記錄影響之行數 */
            row = pres.executeUpdate();
            
            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(pres, conn);
        }

        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);

        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("time", duration);
        response.put("row", row);

        return response;
    }
    
    /**
     * 更新一保險之保險資料
     *
     * @param i 一名保險之Insurance物件
     * @return the JSONObject 回傳SQL指令執行結果與執行之資料
     */
    public JSONObject update(Insurance i) {
        /** 紀錄回傳之資料 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "Update `missa`.`insurance` SET `insurance_name` = ? ,`duration_period` = ? , `amount_insured` = ? , `details` = ? WHERE `insurance_id` = ?";
            /** 取得所需之參數 */
            String insurance_name = i.getInsuranceName();
            int duration_period = i.getDurationPeriod();
            int amount_insured = i.getAmountInsured();
            String details = i.getDetails();
            int insurance_id = i.getInsuranceID();
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, insurance_name);
            pres.setInt(2, duration_period);
            pres.setInt(3, amount_insured);
            pres.setString(4, details);
            pres.setInt(5, insurance_id);
            /** 執行更新之SQL指令並記錄影響之行數 */
            row = pres.executeUpdate();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(pres, conn);
        }
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }
    
}
