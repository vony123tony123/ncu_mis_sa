package ncu.im3069.demo.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
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
@WebServlet("/InsurancePolicyController")
public class InsurancePolicyController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private InsurancePolicyHelper iph = new InsurancePolicyHelper();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 *  取出保單資料
	 *  
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		JsonReader jsr = new JsonReader(request);
		JSONObject jso = jsr.getObject();
		
		if(jso.has("id")) {
			
		}else if (jso.has("member_id")) {
			
		}else {
			
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 *      status=1:
	 *      	保單試算
	 *      status=2:
	 *      	新增保單
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JsonReader jsr = new JsonReader(request);
		JSONObject jso = jsr.getObject();

		if (jso.getInt("status") == 1) {
			JSONArray risk_level = jso.getJSONArray("risk_level");
			int premium = InsurancePolicy.calInsurancePremium(risk_level.getInt(0), risk_level.getInt(1),
					risk_level.getInt(2), risk_level.getInt(3), risk_level.getInt(4), risk_level.getInt(5));

			/** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
			JSONObject resp = new JSONObject();
			resp.put("premium", premium);

			/** 透過 JsonReader 物件回傳到前端（以 JSONObject 方式） */
			jsr.response(resp, response);
		} else if(jso.getInt("status") == 2){
			int member_id = jso.getInt("member_id");
			int insurance_id = jso.getInt("insurance_id");
			String beneficiary_name = jso.getString("beneficiary_name");
			String beneficiary_relation = jso.getString("beneficiary_relation");
			String beneficiary_phone_number = jso.getString("beneficiary_phone_number");
			String beneficiary_address = jso.getString("beneficiary_address");

			InsurancePolicy ip = new InsurancePolicy(member_id, insurance_id, beneficiary_name, beneficiary_relation,
					beneficiary_phone_number, beneficiary_address);
			JSONObject data = iph.create(ip);
			JSONObject resp = new JSONObject();
			
			resp.put("status", 200);
			resp.put("message","成功建立保單...");
			resp.put("data",data);
			
			jsr.response(resp, response);	
		}else {
			/** 以字串組出JSON格式之資料 */
            String resp = "{\"status\": \'400\', \"message\": \'inputstatus error:並非1or2\', \'response\': \'\'}";
            /** 透過JsonReader物件回傳到前端（以字串方式） */
            jsr.response(resp, response);
		}

	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
