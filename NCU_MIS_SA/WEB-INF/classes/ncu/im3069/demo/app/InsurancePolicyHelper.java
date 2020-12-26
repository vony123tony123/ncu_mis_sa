package ncu.im3069.demo.app;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.json.JSONArray;
import org.json.JSONObject;

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
            exexcute_sql = "INSERT INTO `missa`.`insurance_policy`(`insurance_policy_id`, `member_id`, `insurance_id`, `insurance_premium`, `beneficiary_name`, `beneficiary_relationship`, `beneficiary_phone_number`, `beneficiary address`, `create_time`, `modify_time`, `delete_key`)"
            		+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            /** 取得所需之參數 */
            int id = insurancePolicy.getID();
            int member_id = insurancePolicy.getMember_id();
            int insurance_id = insurancePolicy.getInsurance_id();
            
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
    
    
}
