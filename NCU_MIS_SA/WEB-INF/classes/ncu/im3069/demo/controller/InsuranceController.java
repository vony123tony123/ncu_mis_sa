package ncu.im3069.demo.controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.*;
import ncu.im3069.demo.app.Insurance;
import ncu.im3069.demo.app.InsuranceHelper;
import ncu.im3069.tools.JsonReader;

// TODO: Auto-generated Javadoc
/**
 * <p>
 * The Class InsuranceController<br>
 * InsuranceController類別（class）主要用於處理Insurance相關之Http請求（Request），繼承HttpServlet
 * </p>
 * 
 * @author IPLab
 * @version 1.0.0
 * @since 1.0.0
 */
@WebServlet("/api/Insurance.do")
public class InsuranceController extends HttpServlet {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** ih，InsuranceHelper之物件與Insurance相關之資料庫方法（Sigleton） */
    private InsuranceHelper ih =  InsuranceHelper.getHelper();
    
    /**
     * 處理Http Method請求POST方法（新增資料）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        String insurance_name = jso.getString("insurance_name");
        int duration_period = jso.getInt("duration_period");
        int amount_insured = jso.getInt("amount_insured");
        String details = jso.getString("details");
        
        /** 建立一個新的保險物件 */
        Insurance i = new Insurance(insurance_name, duration_period, amount_insured, details);
        
        /** 後端檢查是否有欄位為空值，若有則回傳錯誤訊息 
         * 改至insuranceIndex檢查*/
         
        /** 透過InsuranceHelper物件的create()方法新建一個保險至資料庫 */
        JSONObject data = ih.create(i);
            
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "成功! 新增保險資料...");
        resp.put("response", data);
            
        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
    
    }

    /**
     * 處理Http Method請求GET方法（取得資料）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        /** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
        String insurance_id = jsr.getParameter("insurance_id");
        
        /** 判斷該字串是否存在，若存在代表要取回個別會員之資料，否則代表要取回全部資料庫內會員之資料 */
        if (insurance_id.isEmpty()) {
            /** 透過InsuranceHelper物件之getAll()方法取回所有保險之資料，回傳之資料為JSONObject物件 */
            JSONObject query = ih.getAll();
            
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "所有會員資料取得成功");
            resp.put("response", query);
    
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        }
        else {
            /** 透過InsuranceHelper物件的getByID()方法自資料庫取回該保險之資料，回傳之資料為JSONObject物件 */
            JSONObject query = ih.getByID(insurance_id);
            
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "會員資料取得成功");
            resp.put("response", query);
    
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        }
    }

    /**
     * 處理Http Method請求PUT方法（刪除）
     * 跟原本的刪除做法不同，我們沒有實際刪除資料，而是在刪除欄位把0改成1，故做法和update相同
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void doDelete(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        int delete_key = jso.getInt("delete_key");

        /** 透過傳入之參數，新建一個以這些參數之保險Insurance物件 */
        Insurance i = new Insurance(delete_key);
        
        /** 透過Insurance物件的update()方法至資料庫更新該保險資料，回傳之資料為JSONObject物件 */
        JSONObject data = i.update();
        
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "成功! 刪除保險資料...");
        resp.put("response", data);
        
        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
    }

    /**
     * 處理Http Method請求PUT方法（更新）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void doPut(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        String insurance_name = jso.getString("insurance_name");
        int duration_period = jso.getInt("duration_period");
        int amount_insured = jso.getInt("amount_insured");
        String details = jso.getString("details");

        /** 透過傳入之參數，新建一個以這些參數之保險Insurance物件 */
        Insurance i = new Insurance(insurance_name, duration_period, amount_insured, details);
        
        /** 透過Insurance物件的update()方法至資料庫更新該保險資料，回傳之資料為JSONObject物件 */
        JSONObject data = i.update();
        
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "成功! 更新保險資料...");
        resp.put("response", data);
        
        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
    }
}