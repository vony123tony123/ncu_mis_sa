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
		if (iph == null)
			iph = new InsurancePolicyHelper();
		return iph;
	}

	public JSONObject create(InsurancePolicy insurancePolicy) {
		String exexcute_sql = "";
		int id = -1;
		
		if(checkDuplicateInsurancePolicy(insurancePolicy.getMember_id(), insurancePolicy.getInsurance_id()))
		{
			JSONObject response = new JSONObject();
			response.put("error_message", "同一個會員無法重複投保同一個保險");
			response.put("order_id", id);

			return response;
		}
		
		try {
			/** 取得資料庫之連線 */
			conn = DBMgr.getConnection();
			/** SQL指令 */
			String sql = "INSERT INTO `missa`.`insurance_policy`(`member_id`, `insurance_id`, `insurance_premium`, `beneficiary_name`, `beneficiary_relationship`, `beneficiary_phone_number`, `beneficiary_address`, `create_time`)"
					+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
			/** 取得所需之參數 */
			String member_id = insurancePolicy.getMember_id();
			int insurance_id = insurancePolicy.getInsurance_id();
			int insurance_premium = insurancePolicy.getInsurance_premium();
			String beneficiary_name = insurancePolicy.getBeneficiary_name();
			String beneficiary_relationship = insurancePolicy.getBeneficiary_relationship();
			String beneficiary_phone_number = insurancePolicy.getBeneficiary_phone_number();
			String beneficiary_address = insurancePolicy.getBeneficiary_address();
			Timestamp create_time = insurancePolicy.getCreateTime();

			/** 將參數回填至SQL指令當中 */
			pres = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pres.setString(1, member_id);
			pres.setInt(2, insurance_id);
			pres.setInt(3, insurance_premium);
			pres.setString(4, beneficiary_name);
			pres.setString(5, beneficiary_relationship);
			pres.setString(6, beneficiary_phone_number);
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
			String sql = "SELECT * FROM `missa`.`insurance_policy` WHERE `delete_key` = 0 ";

			/** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
			pres = conn.prepareStatement(sql);
			/** 執行查詢之SQL指令並記錄其回傳之資料 */
			rs = pres.executeQuery();

			/** 紀錄真實執行的SQL指令，並印出 **/
			exexcute_sql = pres.toString();
			System.out.println(exexcute_sql);

			/** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
			while (rs.next()) {
				/** 每執行一次迴圈表示有一筆資料 */
				row += 1;

				/** 將 ResultSet 之資料取出 */
				String insurance_policy_id = rs.getString("insurance_policy_id");
				String member_id = rs.getString("member_id");
				int insurance_id = rs.getInt("insurance_id");
				int insurance_premium = rs.getInt("insurance_premium");
				String beneficiary_name = rs.getString("beneficiary_name");
				String beneficiary_relationship = rs.getString("beneficiary_relationship");
				String beneficiary_phone_number = rs.getString("beneficiary_phone_number");
				String beneficiary_address = rs.getString("beneficiary_address");
				Timestamp create = rs.getTimestamp("create_time");
				Timestamp modify = rs.getTimestamp("modify_time");

				/** 將每一筆保險資料產生一名新Insurance物件 */
				ip = new InsurancePolicy(insurance_policy_id, member_id, insurance_id, insurance_premium, beneficiary_name,
						beneficiary_relationship, beneficiary_phone_number, beneficiary_address, create, modify);
				/** 取出該項保險之資料並封裝至 JSONsonArray 內 */
				jsa.put(ip.getInsurancePolicyInfo());
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

	public JSONObject getByID(String insurancePolicyId) {
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
			/** 取得資料庫之連線 */
			conn = DBMgr.getConnection();
			/** SQL指令 */
			String sql = "SELECT * FROM `missa`.`insurance_policy` "
					+ "WHERE `insurance_policy`.`insurance_policy_id` = ? " + "AND `insurance_policy`.`delete_key`=0";

			/** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
			pres = conn.prepareStatement(sql);
			pres.setString(1, insurancePolicyId);
			/** 執行查詢之SQL指令並記錄其回傳之資料 */
			rs = pres.executeQuery();

			/** 紀錄真實執行的SQL指令，並印出 **/
			exexcute_sql = pres.toString();
			System.out.println(exexcute_sql);

			/** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
			while (rs.next()) {
				/** 每執行一次迴圈表示有一筆資料 */
				row += 1;

				/** 將 ResultSet 之資料取出 */
				String insurance_policy_id = rs.getString("insurance_policy_id");
				String member_id = rs.getString("member_id");
				int insurance_id = rs.getInt("insurance_id");
				int insurance_premium = rs.getInt("insurance_premium");
				String beneficiary_name = rs.getString("beneficiary_name");
				String beneficiary_relationship = rs.getString("beneficiary_relationship");
				String beneficiary_phone_number = rs.getString("beneficiary_phone_number");
				String beneficiary_address = rs.getString("beneficiary_address");
				Timestamp create = rs.getTimestamp("create_time");
				Timestamp modify = rs.getTimestamp("modify_time");

				/** 將每一筆保險資料產生一名新Insurance物件 */
				ip = new InsurancePolicy(insurance_policy_id, member_id, insurance_id, insurance_premium, beneficiary_name,
						beneficiary_relationship, beneficiary_phone_number, beneficiary_address, create, modify);
				/** 取出該項保險之資料並封裝至 JSONsonArray 內 */
				data = ip.getAllInfo();
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
		response.put("data", data);

		return response;
	}

	public JSONObject getByMember_id(String entered_member_id) {
		JSONArray jsa = new JSONArray();
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
			/** 取得資料庫之連線 */
			conn = DBMgr.getConnection();
			/** SQL指令 */
			String sql = "SELECT * FROM `missa`.`insurance_policy` " + "WHERE `insurance_policy`.`member_id` = ? AND "
					+ "`insurance_policy`.`delete_key`=0";

			/** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
			pres = conn.prepareStatement(sql);
			pres.setString(1, entered_member_id);
			/** 執行查詢之SQL指令並記錄其回傳之資料 */
			rs = pres.executeQuery();

			/** 紀錄真實執行的SQL指令，並印出 **/
			exexcute_sql = pres.toString();
			System.out.println(exexcute_sql);

			/** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
			while (rs.next()) {
				/** 每執行一次迴圈表示有一筆資料 */
				row += 1;

				/** 將 ResultSet 之資料取出 */
				String insurance_policy_id = rs.getString("insurance_policy_id");
				String member_id = rs.getString("member_id");
				int insurance_id = rs.getInt("insurance_id");
				int insurance_premium = rs.getInt("insurance_premium");
				String beneficiary_name = rs.getString("beneficiary_name");
				String beneficiary_relationship = rs.getString("beneficiary_relationship");
				String beneficiary_phone_number = rs.getString("beneficiary_phone_number");
				String beneficiary_address = rs.getString("beneficiary_address");
				Timestamp create = rs.getTimestamp("create_time");
				Timestamp modify = rs.getTimestamp("modify_time");

				/** 將每一筆保險資料產生一名新Insurance物件 */
				ip = new InsurancePolicy(insurance_policy_id, member_id, insurance_id, insurance_premium, beneficiary_name,
						beneficiary_relationship, beneficiary_phone_number, beneficiary_address, create, modify);
				/** 取出該項保險之資料並封裝至 JSONsonArray 內 */
				jsa.put(ip.getAllInfo());
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

	public JSONObject update(InsurancePolicy insurancePolicy) {
		JSONObject jso = new JSONObject();
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
			String sql = "Update `missa`.`insurance_policy` SET `beneficiary_name`= ?,"
					+ "`beneficiary_relationship`= ?,`beneficiary_phone_number`= ?,"
					+ "`beneficiary_address`= ? WHERE `insurance_policy_id` =? " + "AND `delete_key`=0";
			/** 取得所需之參數 */
			String insurance_policy_id = insurancePolicy.getID();
			String beneficiary_name = insurancePolicy.getBeneficiary_name();
			String beneficiary_relationship = insurancePolicy.getBeneficiary_relationship();
			String beneficiary_phone_number = insurancePolicy.getBeneficiary_phone_number();
			String beneficiary_address = insurancePolicy.getBeneficiary_address();

			/** 將參數回填至SQL指令當中 */
			pres = conn.prepareStatement(sql);
			pres.setString(1, beneficiary_name);
			pres.setString(2, beneficiary_relationship);
			pres.setString(3, beneficiary_phone_number);
			pres.setString(4, beneficiary_address);
			pres.setString(5, insurance_policy_id);

			/** 執行新增之SQL指令並記錄影響之行數 */
			row = pres.executeUpdate();

			/** 紀錄真實執行的SQL指令，並印出 **/
			exexcute_sql = pres.toString();
			System.out.println(exexcute_sql);
			
			jso = insurancePolicy.getAllInfo();

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

		/** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
		JSONObject response = new JSONObject();
		response.put("sql", exexcute_sql);
		response.put("row", row);
		response.put("time", duration);
		response.put("data", jso);
		return response;
	}
	
	public JSONObject deleteById(int insurance_policy_id) {
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
            
            String sql = "Update `missa`.`insurance_policy` SET `delete_key` = 1 WHERE `insurance_policy_id` = ?";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, insurance_policy_id);
            /** 執行刪除之SQL指令並記錄影響之行數 */
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
            DBMgr.close(rs, pres, conn);
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

        return response;
        
	}
	
	public boolean checkDuplicateInsurancePolicy(String member_id, int insurance_id) {
		int row = 0;
		/** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        try {
        	conn = DBMgr.getConnection();
    		String sql = "SELECT * FROM `insurance_policy` WHERE `member_id` =? AND `insurance_id`=? AND `delete_key` = 0";
    		/** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
			pres.setString(1, member_id);
			pres.setInt(2,insurance_id);
		    /** 執行刪除之SQL指令並記錄影響之行數 */
			rs = pres.executeQuery();
			
			System.out.println(pres.toString());
			
			while(rs.next()) {
				row++;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        if(row>0) {
        	return true;
        }else {
        	return false;
        }
	}
	
	

}
