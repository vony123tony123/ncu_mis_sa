package ncu.im3069.demo.controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.*;
import ncu.im3069.demo.app.Member;
import ncu.im3069.demo.app.MemberHelper;
import ncu.im3069.tools.JsonReader;

// TODO: Auto-generated Javadoc
/**
 * The Class MemberController
 * MemberController類別（class）主要用於處理Member相關之Http請求（Request），繼承HttpServlet
 * 
 * @author IPLab
 * @version 1.0.0
 * @since 1.0.0
 */
@WebServlet("/api/member.do")
public class MemberController extends HttpServlet {

	/**
	 * VARIABLES
	 */
	private static final long serialVersionUID = 1L;
	private MemberHelper mh = MemberHelper.getHelper();

	/**
	 * FUNCTIONS
	 */
	/**
	 * 處理Http Method請求POST方法 （新增資料）
	 *
	 * @param request  Servlet請求之HttpServletRequest之Request物件（前端到後端）
	 * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/**
		 * 透過JsonReader類別將Request之JSON格式資料解析並取回
		 */
		JsonReader jsr = new JsonReader(request);
		JSONObject jso = jsr.getObject();
		/**
		 * 取出經解析到JSONObject之Request參數
		 */
		String id = jso.getString("id");
		String name = jso.getString("name");
		String email = jso.getString("email");
		String password = jso.getString("password");
		String bank_account = jso.getString("bank_account");
		String birthday = jso.getString("birthday");
		int gender = jso.getInt("gender");
		int height = jso.getInt("height");
		int weight = jso.getInt("weight");
		int disease_id = jso.getInt("disease_id");
		String phone_number = jso.getString("phone_number");
		String address = jso.getString("address");
		/**
		 * 建立一個新的會員物件
		 */
		Member m = new Member(id, name, email, password, bank_account, birthday, gender, height, weight, disease_id,
				phone_number, address);
		/**
		 * 後端檢查是否有欄位為空值，若有則回傳錯誤訊息
		 */
		/** 但它回傳"無法連接到伺服器" */
		if (id.isEmpty() || name.isEmpty() || email.isEmpty() || password.isEmpty() || bank_account.isEmpty()
				|| birthday.isEmpty() || String.valueOf( //
						gender).isEmpty()
				|| String.valueOf( //
						height).isEmpty()
				|| String.valueOf( //
						weight).isEmpty()
				|| String.valueOf( //
						disease_id).isEmpty()
				|| phone_number.isEmpty() || address.isEmpty()) {
			/**
			 * 以字串組出JSON格式之資料
			 */
			String resp = "{\"status\": \'400\', \"message\": " + "\'欄位不能有空值\', \'response\': " + "\'\'}";
			/**
			 * 透過JsonReader物件回傳到前端（以字串方式）
			 */
			jsr.response(resp, response);
		}
		/**
		 * 透過MemberHelper物件的checkDuplicate()檢查該會員電子郵件信箱是否有重複
		 */
		else if (!mh.checkDuplicate(m)) {// 做事做事//有!？
			/**
			 * 透過MemberHelper物件的create()方法新建一個會員至資料庫
			 */
			JSONObject data = mh.create(m);// 做事做事//
			/**
			 * 新建一個JSONObject用於將回傳之資料進行封裝
			 */
			JSONObject resp = new JSONObject();
			resp.put("status", "200");
			resp.put("message", "成功! 註冊會員資料...");
			resp.put("response", data);
			/**
			 * 透過JsonReader物件回傳到前端（以JSONObject方式）
			 */
			jsr.response(resp, response);
		} else {
			/**
			 * 以字串組出JSON格式之資料
			 */
			String resp = "{\"status\": \'400\', \"message\": " + "\'新增帳號失敗，" + "此E-Mail帳號重複！\', "
					+ "\'response\': \'\'}";
			/**
			 * 透過JsonReader物件回傳到前端（以字串方式）
			 */
			jsr.response(resp, response);
		}
	}

	/**
	 * 處理Http Method請求PUT方法 （更新資料）
	 *
	 * @param request  Servlet請求之HttpServletRequest之Request物件（前端到後端）
	 * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/**
		 * 透過JsonReader類別將Request之JSON格式資料解析並取回
		 */
		JsonReader jsr = new JsonReader(request);
		JSONObject jso = jsr.getObject();
		/**
		 * 取出經解析到JSONObject之Request參數
		 */
		String name = jso.getString("name");
		String email = jso.getString("email");
		String password = jso.getString("password");
		String bank_account = jso.getString("bank_account");
		String birthday = jso.getString("birthday");
		int gender = jso.getInt("gender");
		int height = jso.getInt("height");
		int weight = jso.getInt("weight");
		int disease_id = jso.getInt("disease_id");
		String phone_number = jso.getString("phone_number");
		String address = jso.getString("address");
		int manager = jso.getInt("manager");
		int delete_key = jso.getInt("delete_key");
		/**
		 * 透過傳入之參數，新建一個以這些參數之會員Member物件
		 */
		Member m = new Member(name, email, password, bank_account, birthday, gender, height, weight, disease_id,
				phone_number, address, manager, delete_key);
		/**
		 * 透過Member物件的update()方法至資料庫更新該名會員資料， 回傳之資料為JSONObject物件
		 */
		JSONObject data = m.update();// 做事做事//
		/**
		 * 新建一個JSONObject用於將回傳之資料進行封裝
		 */
		JSONObject resp = new JSONObject();
		resp.put("status", "200");
		resp.put("message", "成功! 更新會員資料...");
		resp.put("response", data);
		/**
		 * 透過JsonReader物件回傳到前端（以JSONObject方式）
		 */
		jsr.response(resp, response);
	}

	/**
	 * 處理Http Method請求GET方法 （取得資料）
	 *
	 * @param request  Servlet請求之HttpServletRequest之Request物件（前端到後端）
	 * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/**
		 * 透過JsonReader類別將Request之JSON格式資料解析並取回
		 */
		JsonReader jsr = new JsonReader(request);
		/**
		 * 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數， 可以直接由此方法取回資料
		 */
		String ID_number = jsr.getParameter("ID_number");
		String password = jsr.getParameter("password");
		/**
		 * 判斷該字串是否存在，若存在代表要取回個別會員之資料， 否則代表要取回全部資料庫內會員之資料
		 */
		if (ID_number.isEmpty()) {
			/**
			 * 透過MemberHelper物件之getAll()方法取回所有會員之資料， 回傳之資料為JSONObject物件
			 */
			JSONObject query = mh.getAll();// 做事做事//
			/**
			 * 新建一個JSONObject用於將回傳之資料進行封裝
			 */
			JSONObject resp = new JSONObject();
			resp.put("status", "200");
			resp.put("message", "所有會員資料取得成功");
			resp.put("response", query);
			/**
			 * 透過JsonReader物件回傳到前端（以JSONObject方式）
			 */
			jsr.response(resp, response);
		} else if(password.isEmpty()){
			/**
			 * 透過MemberHelper物件的getByID()方法自資料庫取回該名會員之資料， 回傳之資料為JSONObject物件
			 */
			JSONObject query = mh.getByID(ID_number);// 做事做事//
			/**
			 * 新建一個JSONObject用於將回傳之資料進行封裝
			 */
			JSONObject resp = new JSONObject();
			resp.put("status", "200");
			resp.put("message", "會員資料取得成功");
			resp.put("response", query);
			/**
			 * 透過JsonReader物件回傳到前端（以JSONObject方式）
			 */
			jsr.response(resp, response);
		}else {
			JSONObject query = mh.checkLogin(ID_number, password);
			
			System.out.println((int)query.get("row"));
			
			JSONObject resp = new JSONObject();
			if((int)query.get("row") > 0) {
				resp.put("status", "200");
				resp.put("message", "會員驗證成功");
				resp.put("response", query);
			}else {
				resp.put("status", "201");
				resp.put("message", "會員驗證失敗");
			}
			
			jsr.response(resp, response);
		}
	}

	/**
	 * 處理Http Method請求DELETE方法 （刪除資料）
	 *
	 * @param request  Servlet請求之HttpServletRequest之Request物件（前端到後端）
	 * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	public void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/**
		 * 透過JsonReader類別將Request之JSON格式資料解析並取回
		 */
		JsonReader jsr = new JsonReader(request);
		JSONObject jso = jsr.getObject();
		/**
		 * 取出經解析到JSONObject之Request參數
		 */
		String id = jso.getString("id");
		/**
		 * 透過MemberHelper物件的deleteByID()方法至資料庫刪除該名會員， 回傳之資料為JSONObject物件
		 */
		JSONObject query = mh.deleteByID(id);// 做事做事//
		/**
		 * 新建一個JSONObject用於將回傳之資料進行封裝
		 */
		JSONObject resp = new JSONObject();
		resp.put("status", "200");
		resp.put("message", "會員移除成功！");
		resp.put("response", query);

		/**
		 * 透過JsonReader物件回傳到前端（以JSONObject方式）
		 */
		jsr.response(resp, response);
	}

}