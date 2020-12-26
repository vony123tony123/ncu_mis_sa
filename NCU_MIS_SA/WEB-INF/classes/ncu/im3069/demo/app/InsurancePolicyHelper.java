package ncu.im3069.demo.app;

import java.sql.*;
import java.util.*;
import org.json.*;

import ncu.im3069.demo.util.DBMgr;

public class InsurancePolicyHelper {
	private static InsurancePolicyHelper iph;
    private Connection conn = null;
    private PreparedStatement pres = null;
    
	public static InsurancePolicyHelper getHelper() {
		if(iph==null) iph = new InsurancePolicyHelper();
		return iph;
	}
	
	public JSONObject create(InsurancePolicy insurancePolicy) {
		String exexcute_sql = "";
		int id = -1;
        try {
        	/** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "INSERT INTO `missa`.`insurance_policy`( `member_id`, `insurance_id`, `insurance_premium`, `beneficiary_name`, `beneficiary_relationship`, `beneficiary_phone_number`, `beneficiary address`, `create_time`)"
            		+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            /** 取得所需之參數 */
            int member_id = insurancePolicy.getMember_id();
            int insurance_id = insurancePolicy.getInsurance_id();
            int insurance_preimum = insurancePolicy.getInsurance_preimum();
            String beneficiary_name = insurancePolicy.getBeneficiary_name();
            String beneficiary_relationship = insurancePolicy.getBeneficiary_relationship();
            String beneficiary_phone_number = insurancePolicy.getBeneficiary_phone_number();
            String beneficiary_address = insurancePolicy.getBeneficiary_address();
            Timestamp create_time = insurancePolicy.getCreateTime();
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pres.setInt(1, member_id);
            pres.setInt(2, insurance_id);
            pres.setInt(3, insurance_preimum);
            pres.setString(4, beneficiary_name);
            pres.setString(5, beneficiary_relationship);
            pres.setString(6,beneficiary_phone_number);
            pres.setString(7, beneficiary_address);
            pres.setTimestamp(8, create_time);
            
            /** 執行新增之SQL指令並記錄影響之行數 */
            pres.executeUpdate();
            
            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            ResultSet rs = pres.getGeneratedKeys();
            
            if (rs.next()) {
                id = rs.getInt(1);  
            }
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
        
        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("order_id", id);

        return response;
	}
	
	public JSONObject getAll() {
		InsurancePolicy ip = null;
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
            String sql = "SELECT * FROM `missa`.`insurance_policy` WHERE `delete_key`=0";
            
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
                int id = rs.getInt("insurance_policy_id");
                int member_id = rs.getInt("member_id");
                int insurance_id = rs.getInt("insurance_id");
                int insurance_preimum = rs.getInt("insurance_preimum");
                String beneficiary_name = rs.getString("beneficiary_name");
                String beneficiary_relationship = rs.getString("beneficiary_relationship");
                String beneficiary_phone_number = rs.getString("beneficiary_phone_number");
                String beneficiary_address = rs.getString("beneficiary_address");
                Timestamp create = rs.getTimestamp("create_time");
                Timestamp modify = rs.getTimestamp("modify_time");
                
                /** 將每一筆保險資料產生一名新Insurance物件 */
                ip = new InsurancePolicy(id, member_id, insurance_id, insurance_preimum, beneficiary_name,beneficiary_relationship , beneficiary_phone_number,beneficiary_address , create, modify);
                /** 取出該項保險之資料並封裝至 JSONsonArray 內 */
                jsa.put(ip.getAllInfo());
            }
        	
        }catch (SQLException e) {
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
	
	public JSONObject getByID(int id) {
		JSONObject data = new JSONObject();
		InsurancePolicy ip = null;
		/** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
        	
        	
        }catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(rs, pres, conn);
        }
	}
	
	
    
    
}
