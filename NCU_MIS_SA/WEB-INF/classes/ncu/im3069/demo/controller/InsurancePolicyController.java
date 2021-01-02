package ncu.im3069.demo.controller;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.webresources.ClasspathURLStreamHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ncu.im3069.demo.app.Insurance;
import ncu.im3069.demo.app.InsurancePolicy;
import ncu.im3069.demo.app.InsurancePolicyHelper;
import ncu.im3069.demo.app.Order;
import ncu.im3069.demo.app.Product;
import ncu.im3069.tools.JsonReader;

/**
 * Servlet implementation class InsurancePolicyController
 */
@WebServlet("/api/insurancePolicy.do")
public class InsurancePolicyController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private InsurancePolicyHelper iph = new InsurancePolicyHelper();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response) 
	 * 取出保單資料
	 * 
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		JsonReader jsr = new JsonReader(request);

		if (!jsr.getParameter("insurance_policy_id").isEmpty()) {
			JSONObject query = iph.getByID(jsr.getParameter("insurance_policy_id"));

			JSONObject resp = new JSONObject();
			resp.put("status", 200);
			resp.put("message", "成功獲取保單資料...");
			resp.put("response", query);

			jsr.response(resp, response);
		} else if (!jsr.getParameter("member_id").isEmpty()) {
			JSONObject query = iph.getByMember_id(jsr.getParameter("member_id"));

			JSONObject resp = new JSONObject();
			resp.put("status", 200);
			resp.put("message", "成功獲取特定會員保單資料...");
			resp.put("response", query);

			jsr.response(resp, response);
		} else {
			JSONObject query = iph.getAll();

			JSONObject resp = new JSONObject();
			resp.put("status", 200);
			resp.put("message", "成功獲取所有保單資料...");
			resp.put("response", query);

			jsr.response(resp, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response) status=1: 保單試算 status=2: 新增保單
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JsonReader jsr = new JsonReader(request);
		JSONObject jso = jsr.getObject();

		if (jso.getInt("status") == 1) {
			int gender = jso.getInt("gender");
			String birthday = jso.getString("birthday");
			int height = jso.getInt("height");
			int weight = jso.getInt("weight");
			int disease_id = jso.getInt("disease_id");
			int amount_insured = jso.getInt("amount_insured");
			int premium=0;
			try {
				premium = InsurancePolicy.calInsurancePremium(gender,birthday, height, weight, disease_id, amount_insured);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
			JSONObject resp = new JSONObject();
			resp.put("status", 200);
			resp.put("message", "成功試算金額...");
			resp.put("response", premium);

			/** 透過 JsonReader 物件回傳到前端（以 JSONObject 方式） */
			jsr.response(resp, response);
			
		} else if (jso.getInt("status") == 2) {
			String member_id = jso.getString("member_id");
			int insurance_id = jso.getInt("insurance_id");
			int insurance_premium = jso.getInt("insurance_premium");
			String beneficiary_name = jso.getString("beneficiary_name");
			String beneficiary_relation = jso.getString("beneficiary_relationship");
			String beneficiary_phone_number = jso.getString("beneficiary_phone_number");
			String beneficiary_address = jso.getString("beneficiary_address");

			InsurancePolicy ip=null;
			try {
				ip = new InsurancePolicy(member_id, insurance_id, insurance_premium, beneficiary_name, beneficiary_relation,
						beneficiary_phone_number, beneficiary_address);
			} catch (JSONException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JSONObject data = iph.create(ip);
			JSONObject resp = new JSONObject();

			resp.put("status", 200);
			resp.put("message", "成功建立保單...");
			resp.put("response", data);

			jsr.response(resp, response);
		} else {
			/** 以字串組出JSON格式之資料 */
			String resp = "{\"status\": \'400\', \"message\": \'inputstatus error:並非1or2\', \'response\': \'\'}";
			/** 透過JsonReader物件回傳到前端（以字串方式） */
			jsr.response(resp, response);
		}

	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 * 
	 *      更新保單資料
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
		JsonReader jsr = new JsonReader(request);
		JSONObject jso = jsr.getObject();

		String insurance_policy_id = jso.getString("insurance_policy_id");
		String beneficiary_name = jso.getString("beneficiary_name");
		String beneficiary_relationship = jso.getString("beneficiary_relationship");
		String beneficiary_phone_number = jso.getString("beneficiary_phone_number");
		String beneficiary_address = jso.getString("beneficiary_address");

		InsurancePolicy ip = new InsurancePolicy(insurance_policy_id, beneficiary_name, beneficiary_relationship,
				beneficiary_phone_number, beneficiary_address);

		JSONObject data = iph.update(ip);
		
		/** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "成功! 更新保單資料...");
        resp.put("response", data);
        
        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);

	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
		JsonReader jsr = new JsonReader(request);
		JSONObject jso = jsr.getObject();
		
		/** 取出經解析到JSONObject之Request參數 */
        int insurancePolicy_id = jso.getInt("insurancePolicy_id");
        
        /** 透過MemberHelper物件的deleteByID()方法至資料庫刪除該名會員，回傳之資料為JSONObject物件 */
		JSONObject query = iph.deleteById(insurancePolicy_id);
		
		 /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "保單移除成功！");
        resp.put("response", query);

        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
	}

}
