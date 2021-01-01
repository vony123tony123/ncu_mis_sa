package ncu.im3069.demo.app;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class InsurancePolicy {
	
	private String insurance_policy_id;
	private String member_id;
	private JSONObject member;
	private int insurance_id;
	private JSONObject insurance;
	private int insurance_premium;
	private String beneficiary_name;
	private String beneficiary_relationship;
	private String beneficiary_phone_number;
	private String beneficiary_address;
	private Timestamp create_time = Timestamp.valueOf(LocalDateTime.now());
	private Timestamp modify_time = null;
	
	private MemberHelper mh=MemberHelper.getHelper();
	private InsuranceHelper ih = InsuranceHelper.getHelper();
	private InsurancePolicyHelper iph = InsurancePolicyHelper.getHelper();
	
	
	
	
	public InsurancePolicy(String id, String beneficiary_name, String beneficiary_relationship,
			String beneficiary_phone_number, String beneficiary_address) {
		this.insurance_policy_id = id;
		this.beneficiary_name = beneficiary_name;
		this.beneficiary_relationship = beneficiary_relationship;
		this.beneficiary_phone_number = beneficiary_phone_number;
		this.beneficiary_address = beneficiary_address;
		getMemberFromDB();
		getInsuranceFromDB();
	}

	public InsurancePolicy(String member_id, int insurance_id, String beneficiary_name,
			String beneficiary_relation, String beneficiary_phone_number, String beneficiary_address) throws JSONException, ParseException 
	{
		this.member_id = member_id;
		this.insurance_id = insurance_id;
		this.beneficiary_name = beneficiary_name;
		this.beneficiary_relationship = beneficiary_relation;
		this.beneficiary_phone_number = beneficiary_phone_number;
		this.beneficiary_address = beneficiary_address;
		getMemberFromDB();
		getInsuranceFromDB();
		this.insurance_premium = calInsurancePremium();
	}

	public InsurancePolicy(String id, String member_id, int insurance_id,
			int insurance_premium, String beneficiary_name, String beneficiary_relationship,
			String beneficiary_phone_number, String beneficiary_address, Timestamp create_time, Timestamp modify_time) {
		this.insurance_policy_id = id;
		this.member_id = member_id;
		this.insurance_id = insurance_id;
		this.insurance_premium = insurance_premium;
		this.beneficiary_name = beneficiary_name;
		this.beneficiary_relationship = beneficiary_relationship;
		this.beneficiary_phone_number = beneficiary_phone_number;
		this.beneficiary_address = beneficiary_address;
		this.create_time = create_time;
		this.modify_time = modify_time;
		getMemberFromDB();
		getInsuranceFromDB();
	}



	private int calInsurancePremium() throws JSONException, ParseException {
		int gender = member.getInt("gender");
		String birthday = member.getString("birthday");
		int height = member.getInt("height");
		int weight = member.getInt("weight");
		int disease_id = member.getInt("disease_id");
		int amount_insured = insurance.getInt("amount_insured");
		return calInsurancePremium(gender, birthday, height, weight, disease_id, amount_insured);
	}
	
	public static int calInsurancePremium(int gender, String birthday, int height, int weight, int disease_id,int amount_insured) throws ParseException {
		double premium_level = 0;//計算風險貼水
		double bmi = weight/((height/100)^2);
		int age = 0;
		int premium = 0;
		
		DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
		Date date = sdf.parse(birthday);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int birthyear = cal.get(Calendar.YEAR);
		
		age = LocalDateTime.now().getYear()-birthyear;
		
		if(bmi>24) 
			premium_level += bmi-24;
				
		if(bmi<18.5) 
			premium_level += 18.5-bmi;
		
		if(age > 40) 
			premium_level += age-40;
		
		if(age < 20) 
			premium_level += 20-age;
		
		premium_level += (5 + disease_id * 5);
		
		if(gender == 1) 
			premium_level *= 2;
		
		premium = (int) (amount_insured/ 2500 * premium_level);
		
		return premium;
	}

	
	public String getID() {
		return insurance_policy_id;
	}

	public String getMember_id() {
		return member_id;
	}

	public int getInsurance_id() {
		return insurance_id;
	}

	public int getInsurance_premium() {
		return insurance_premium;
	}

	public String getBeneficiary_name() {
		return beneficiary_name;
	}

	public String getBeneficiary_relationship() {
		return beneficiary_relationship;
	}

	public String getBeneficiary_phone_number() {
		return beneficiary_phone_number;
	}

	public String getBeneficiary_address() {
		return beneficiary_address;
	}

	public Timestamp getCreateTime() {
		return create_time;
	}
	
	public String getCreateTimeString() {
		return create_time.toGMTString();
	}

	public Timestamp getModify_time() {
		return modify_time;
	}

	public String getModify_timeString() {
		return modify_time.toGMTString();
	}
	
	public void setModify_time(Timestamp modify_time) {
		this.modify_time = modify_time;
	}

	public void getMemberFromDB() {
		this.member = mh.getByID(getMember_id());
	}
	
	public void getInsuranceFromDB() {
		this.insurance = ih.getByID(String.valueOf(getInsurance_id()));
	}
	
	
	public JSONObject getMemberInfo() {
		return member;
	}

	public JSONObject getInsuranceInfo() {
		return insurance;
	}
	
	public JSONObject getInsurancePolicyInfo() {
		JSONObject jso = new JSONObject();
		jso.put("insurance_policy_id", getID());
		jso.put("member_id", getMember_id());
		jso.put("insurance_id", getInsurance_id());
		jso.put("insurance_premium", getInsurance_premium());
		jso.put("beneficiary_name", getBeneficiary_name());
		jso.put("beneficiary_relationship", getBeneficiary_relationship());
		jso.put("beneficiary_phone_number", getBeneficiary_phone_number());
		jso.put("beneficiary_address", getBeneficiary_address());
		jso.put("create_time", getCreateTimeString());
		jso.put("modify_time", getModify_timeString());
		return jso;
	}
	
	public JSONObject getAllInfo() {
		JSONObject jso = new JSONObject();
		jso.put("InsuranceInfo", getInsuranceInfo());
		jso.put("MemberInfo", getMemberInfo());
		jso.put("InsurancePolicyInfo", getInsurancePolicyInfo());
		return jso;
	}
	
	public JSONObject update() {
		JSONObject jso = new JSONObject();
		if(this.insurance_policy_id != null) {
			jso = iph.update(this);	
		}
		return jso;
	}

}
