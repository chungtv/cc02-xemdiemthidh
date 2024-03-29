package sobaodanh;

import java.io.IOException;
import java.util.StringTokenizer;

import android.os.*;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.xemdiemthidaihoc.R;

public class ChiTietThiSinh extends Activity /*implements Runnable*/ {
	private static final int chi_tiet = 1;
	private final Handler handler = new Handler();
	private static final String SOAP_ACTION = "http://tempuri.org/IXDTService/GetThiSinhDetails";
    private static final String METHOD_NAME = "GetThiSinhDetails";
    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String URL = "http://10.0.2.2:3744/XemDiemThiService.svc";    
    private ProgressDialog pd;
    private TextView view;
    private String sbd;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.thisinhdetail); 
        view = (TextView) this.findViewById(R.id.TextView01);
    	pd = ProgressDialog.show(this, "Đang nhận dữ liệu....",    			
    			"Lấy dữ liệu", true, false);
    	
    	doProcess();
    }
    
    public void doProcess()
    {
        handler.post(new Runnable() {
            public void run(){
        ///////////////////////////////////////////////////////////////////
        sbd = getIntent().getStringExtra("sbd");
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("soBaoDanh", sbd);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;             
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        
        try{
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result=(SoapObject)envelope.getResponse();            
            ChiTietThiSinh.this.setTitle(" Thí sinh : " + result.getProperty("HoTen"));
            String ngaysinh = new StringTokenizer(result.getProperty("NgaySinh").toString(), "T").nextToken();
            view.setText("\tNgày sinh : " + ngaysinh + "\n" +
                    "\tSố Báo Danh : " + result.getProperty("SoBaoDanh") + "\n" +
                    "\tTên ngành : " + result.getProperty("TenNganh") + "\n" +
                    "\tTên trường :" + result.getProperty("TenTruong") + "\n" +
                    "\tĐiểm môn một\t: " + result.getProperty("Diem1") + "\n" +
                    "\tĐiểm môn hai\t: " + result.getProperty("Diem2") + "\n" +
                    "\tĐiểm môn ba \t: " + result.getProperty("Diem3") + "\n");	            
	        pd.dismiss();
	        }catch(Exception e){
	            view.setText("Mả số thí sinh không đúng");
	            pd.dismiss();           
		    }
            }
        });
    }
}
