package ncu.im3069.demo.app;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.json.JSONObject;

public class InsurancePolicy {
	
	private int id;
	private int member_id;
	private JSONObject member;
	private int insurance_id;
	private JSONObject insurance;
	private int insurance_preimum;
	private String beneficiary_name;
	private String beneficiary_relation;
	private String beneficiary_phone_number;
	private String beneficiary_address;
	private Timestamp create=Timestamp.valueOf(LocalDateTime.now());
	
	private MemberHelper mh=MemberHelper.getHelper();
	private InsuranceHelper ih = InsuranceHelper.getHelper();
	private InsurancePolicyHelper iph = InsurancePolicyHelper.getHelper();
	
	public InsurancePolicy(int id, int memeber_id, int insurance_id, String beneficiary_name,
			String beneficiary_relation, String beneficiary_phone_number, String beneficiary_address) 
	{
		this.id = id;
		this.member_id = memeber_id;
		this.insurance_id = insurance_id;
		this.beneficiary_name = beneficiary_name;
		this.beneficiary_relation = beneficiary_relation;
		this.beneficiary_phone_number = beneficiary_phone_number;
		this.beneficiary_address = beneficiary_address;
		getMemberFromDB();
		getInsuranceFromDB();
	}

	public int getID() {
		return id;
	}

	public int getMember_id() {
		return member_id;
	}

	public int getInsurance_id() {
		return insurance_id;
	}

	public int getInsurance_preimum() {
		return insurance_preimum;
	}

	public String getBeneficiary_name() {
		return beneficiary_name;
	}

	public String getBeneficiary_relation() {
		return beneficiary_relation;
	}

	public String getBeneficiary_phone_number() {
		return beneficiary_phone_number;
	}

	public String getBeneficiary_address() {
		return beneficiary_address;
	}

	public Timestamp getCreate() {
		return create;
	}

	public void getMemberFromDB() {
		this.member = mh.getByID(getID());
	}
	
	public void getInsuranceFromDB() {
		this.insurance = ih.getByID(getID());
	}
	
	
	public JSONObject getMemberInfo() {
		return member;
	}

	public JSONObject getInsuranceInfo() {
		return insurance;
	}
	
	public JSONObject getInsurancePolicyInfo() {
		JSONObject jso = new JSONObject();
		jso.put("id", getID());
		jso.put("insurance_preimum", getInsurance_preimum());
		jso.put("beneficiary_name", getBeneficiary_name());
		jso.put("beneficiary_relation", getBeneficiary_relation());
		jso.put("beneficiary_phone_number", getBeneficiary_phone_number());
		jso.put("beneficiary_address", getBeneficiary_address());
		return jso;
	}
	
	public JSONObject getAllInfo() {
		JSONObject jso = new JSONObject();
		jso.put("InsuranceInfo", getInsuranceInfo());
		jso.put("MemberInfo", getMemberInfo());
		jso.put("InsurancePolicyInfo", getInsurancePolicyInfo());
	}
	
	public JSONObject update() {
		JSONObject jso = new JSONObject();
		if(this.id != 0) {
			jso = iph.update(this);
		}
		return jso;
	}

}
