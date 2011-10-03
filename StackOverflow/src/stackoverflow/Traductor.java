package stackoverflow;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.json.JSONException;
import org.json.JSONObject;


public class Traductor implements FlavorListener, ClipboardOwner{

	
	Text toTranslate;
	Text translated;
	Button idiomaEs;
	static Clipboard clipboard;
	String contenido;
	
		
	public Traductor(TabItem item){
		final Composite parent = new Composite(item.getParent(), SWT.NONE);
		item.setText("Traductor");
		item.setControl(parent);
		
		final FormLayout form = new FormLayout();
		parent.setLayout(form);

		//SASH
		final Sash sash = new Sash (parent, SWT.HORIZONTAL | SWT.BORDER);	
		final FormData sashData = new FormData();
		sashData.left = new FormAttachment(0, 0);
		sashData.top = new FormAttachment(52, 0);
		sashData.right = new FormAttachment(100, 0);
		sash.setLayoutData(sashData);
		
		//LABEL TRADUCIR
		Label traducir = new Label(parent, SWT.NONE);
		traducir.setText("Traducir:");
		FormData data = new FormData();
		data.left = new FormAttachment(0, 5);
		data.top = new FormAttachment(0,5);
		traducir.setLayoutData(data);

		//BUTTON
		final Button translateButton = new Button(parent, SWT.PUSH);
		translateButton.setText("Traducir");
		data = new FormData();
		data.bottom = new FormAttachment(sash,-5);
		data.right = new FormAttachment(100,-10);
		translateButton.setLayoutData(data);
		
		//TEXTO TOTRANSLATE
		toTranslate = new Text(parent, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);
		data = new FormData();
		data.left = new FormAttachment(traducir, 5);
		data.top = new FormAttachment(traducir,5);
		data.bottom = new FormAttachment(translateButton,-5);
		data.right = new FormAttachment(100,-10);
		toTranslate.setLayoutData(data);		
		
		//Seleccion de idioma
		idiomaEs = new Button(parent, SWT.RADIO);
		idiomaEs.setText("español");
		
		Button idiomaIn = new Button(parent, SWT.RADIO);
		idiomaIn.setText("inglés");
		idiomaEs.setSelection(true);
		
		data = new FormData();
		data.right = new FormAttachment(translateButton,-10);
		data.bottom = new FormAttachment(sash,-5);
		idiomaEs.setLayoutData(data);
		data = new FormData();
		data.right = new FormAttachment(idiomaEs,-5);
		data.bottom = new FormAttachment(sash,-5);
		idiomaIn.setLayoutData(data);
	
		//LABEL SELECCION
		Label idioma = new Label(parent, SWT.NONE);
		idioma.setText("Traducir a:");
		data= new FormData();
		data.right = new FormAttachment(idiomaIn, -10);
		data.bottom = new FormAttachment(sash, -5);
		idioma.setLayoutData(data);
		
		//LABEL TRADUCIDO
		Label traducido = new Label (parent, SWT.NONE);
		traducido.setText("Traducción");
		data = new FormData();
		data.left = new FormAttachment(0, 10);
		data.top = new FormAttachment(sash, 5);
		traducido.setLayoutData(data);
		
		//TEXTO TRANSLATED
		translated = new Text(parent, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);
		data = new FormData();
		data.left = new FormAttachment(traducido, 5);
		data.top = new FormAttachment(traducido,5);
		data.bottom = new FormAttachment(100,-5);
		data.right = new FormAttachment(100,-10);
		translated.setLayoutData(data);		
		
		
		sash.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
	            sash.setBounds(e.x, e.y, e.width, e.height);
	            sashData.top = new FormAttachment(0, e.y);
	            sashData.left = new FormAttachment(0, 0);
	            sashData.right = new FormAttachment(100, 0);
	            sashData.height = 3;
				parent.layout();
				
			}			
		});
		
		
		
		
		Listener listener = new Listener() {
			public void handleEvent(Event e) {
				Widget w = e.widget;
				String to;
				String from;
				if(w == translateButton){
					if(idiomaEs.getSelection()){
						to = "en";
						from="es";
					}else{
						to="es";
						from="en";
					}
					String q = toTranslate.getText();
					String resultado = doTranslate(q, to,from);
					translated.setText(resultado);
					
				}
			}
		};
		
		translateButton.addListener(SWT.Selection, listener);
		
		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.addFlavorListener(this);

		
	}
	
	
	
	/**
	    * Call the Google Translation API to translate a string from one
	    * language to another. For more info on the API see:
	    * http://code.google.com/apis/ajaxlanguage
	    */
	   private String doTranslate(String original, String from,
	         String to) {
	      String result = "error";
	      HttpURLConnection con = null;
	     
	      try {
	         // Check if task has been interrupted
	         if (Thread.interrupted())
	            throw new InterruptedException();

	         // Build RESTful query for Google API
	         String q = URLEncoder.encode(original, "UTF-8");
	         URL url = new URL(
	               "http://ajax.googleapis.com/ajax/services/language/translate"
	                     + "?v=1.0" + "&q=" + q + "&langpair=" + from
	                     + "%7C" + to);
	         con = (HttpURLConnection) url.openConnection();
	         con.setReadTimeout(10000 /* milliseconds */);
	         con.setConnectTimeout(15000 /* milliseconds */);
	         con.setRequestMethod("GET");
	         con.addRequestProperty("Referer",
	               "http://www.pragprog.com/titles/eband3/hello-android");
	         con.setDoInput(true);

	         // Start the query
	         con.connect();

	         // Check if task has been interrupted
	         if (Thread.interrupted())
	            throw new InterruptedException();

	         // Read results from the query
	         BufferedReader reader = new BufferedReader(
	               new InputStreamReader(con.getInputStream(), "UTF-8"));
	         String payload = reader.readLine();
	         reader.close();

	         // Parse to get translated text
	         JSONObject jsonObject = new JSONObject(payload);
	         result = jsonObject.getJSONObject("responseData")
	               .getString("translatedText")
	               .replace("&#39;", "'")
	               .replace("&amp;", "&");

	         // Check if task has been interrupted
	         if (Thread.interrupted())
	            throw new InterruptedException();

	      } catch (IOException e) {
	        // Log.e(TAG, "IOException", e);
	      } catch (JSONException e) {
	        // Log.e(TAG, "JSONException", e);
	      } catch (InterruptedException e) {
	        // Log.d(TAG, "InterruptedException", e);
	        /* result = translate.getResources().getString(
	               R.string.translation_interrupted);*/
	      } finally {
	         if (con != null) {
	            con.disconnect();
	         }
	      }

	      // All done
	      //Log.d(TAG, "   -> returned " + result);
	      return result;
	   }

	   //clipboard
	   public static String getClipboardContents() {
		   String result = "";
		   //clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	   
	  
		   //odd: the Object param of getContents is not currently used
		   Transferable contents = clipboard.getContents(null);
		   boolean hasTransferableText =
		     (contents != null) &&
		     contents.isDataFlavorSupported(DataFlavor.stringFlavor)
		   ;
		   if ( hasTransferableText ) {
		     try {
		       result = (String)contents.getTransferData(DataFlavor.stringFlavor);
		     }
		     catch (UnsupportedFlavorException ex){
		       //highly unlikely since we are using a standard DataFlavor
		       System.out.println(ex);
		       ex.printStackTrace();
		     }
		     catch (IOException ex) {
		       System.out.println(ex);
		       ex.printStackTrace();
		     }
		   }
		   return result;
		 }



	@Override
	public void flavorsChanged(FlavorEvent e) {
		System.out.println("printea \n");
		clipboard.removeFlavorListener(this);
		
		contenido = getClipboardContents();
		clipboard.setContents(clipboard.getContents(null), this);
		
		Runnable copyRun = new Runnable() {
			
			@Override
			public void run() {
				toTranslate.setText(contenido);		
			}
		};
		
		clipboard.addFlavorListener(this);
		
		Display.getDefault().asyncExec(copyRun);
		
				
	}



	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}