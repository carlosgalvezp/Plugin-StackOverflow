                                                                     
                                                                     
                                                                     
                                             
package stackoverflow;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;


public class Buscador {

	Text searchBarP, searchBarT;
	
		Browser browser;
		Text htmlText, scriptText;
		Label pregunta, tag;
		
		Button searchButton, htmlButton, scriptButton;
		static String html = 
			"<html>\r\n"+
			"	<body>\r\n"+
			"		<h1 id='myid'>HTML Document</h1>\r\n"+
			"		<h2>Set HTML content</h2>\r\n"+
			"		<ol>\r\n"+
			"			<li>Enter html data into the 'setText' pane</li>\r\n"+
			"			<li>Click on 'setText' to set the new content</li>\r\n"+
			"		</ol>\r\n"+
			"		<h2>Query or modify HTML document</h2>\r\n"+
			"		<ol>\r\n"+
			"		<li>Enter javascript commands into the 'execute' pane</li>\r\n"+
			"		<li>Click on 'execute' to run the javascript in the current document</li>\r\n"+
			"		</ol>\r\n"+
			"	</body>\r\n"+
			"</html>";
		
		static String script = 
			"var node = document.createElement('P');\r\n"+
			"var text = document.createTextNode('Content inserted!');\r\n"+
			"node.appendChild(text);\r\n"+
			"document.getElementById('myid').appendChild(node);\r\n\r\n"+
			"document.bgColor = 'yellow';";
	
	public Buscador(TabItem item){
		final Composite parent = new Composite(item.getParent(), SWT.NONE);
		item.setText("Buscador");
		item.setControl(parent);
		
		try {
			browser = new Browser(parent, SWT.NONE);
		} catch (SWTError e) {
			e.printStackTrace();
			return;
		}
		
		
		final Sash sash = new Sash(parent, SWT.VERTICAL);
		Composite panel = new Composite(parent, SWT.NONE);
		final FormLayout form = new FormLayout();
		parent.setLayout(form);
		
		
		//TEXTO Y BUSCADOR DE PREGUNTAS
		pregunta = new Label(parent, SWT.NONE);
		pregunta.setText("Pregunta:");
		FormData data = new FormData();
		data.left = new FormAttachment(0, 5);
		data.top = new FormAttachment(0,5);
		pregunta.setLayoutData(data);
		
		searchBarP = new Text(parent, SWT.BORDER);
		data = new FormData();
		data.left = new FormAttachment(pregunta, 5);
		data.right = new FormAttachment(80, 0);
		data.top = new FormAttachment(0, 5);
		searchBarP.setLayoutData(data);
		
		
		//TEXTO Y BUSCADOR DE TAGS
		tag = new Label(parent, SWT.NONE);
		tag.setText("Tag:");
		data = new FormData();
		data.left = new FormAttachment(0, 5);
		data.top = new FormAttachment(searchBarP,5);
		tag.setLayoutData(data);
		
		searchBarT = new Text(parent, SWT.BORDER);
		data = new FormData();
		data.left = new FormAttachment(tag, 5);
		data.right = new FormAttachment(80, 0);
		data.top = new FormAttachment(searchBarP, 5);
		searchBarT.setLayoutData(data);
		
		//BOTON BUSCADOR
		searchButton = new Button(parent, SWT.PUSH);
		searchButton.setText("Buscar");
		data = new FormData();
		data.right = new FormAttachment(100, -10);
		data.top = new FormAttachment(searchBarT, 10);
		searchButton.setLayoutData(data);
		
		
		
		final FormData sashData = new FormData();
		sashData.left = new FormAttachment(30, 0);
		sashData.top = new FormAttachment(searchButton, 0);
		sashData.bottom = new FormAttachment(100, 0);
		sash.setLayoutData(sashData);
		sash.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				Rectangle rect = sash.getBounds();
				Rectangle parentRect = sash.getParent().getClientArea();
				int right = parentRect.width - rect.width - 20;
				e.x = Math.max(Math.min(e.x, right), 20);
				if (e.x != rect.x) {
					sashData.left = new FormAttachment(0, e.x);
					parent.layout();
				}
			}			
		});
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(sash, 0);
		data.top = new FormAttachment(10, 0);
		data.bottom = new FormAttachment(100, 0);
		panel.setLayoutData(data);
		
		//BROWSER
		data = new FormData();
		data.left = new FormAttachment(sash, 0);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(searchButton,0);
		data.bottom = new FormAttachment(100, 0);
		browser.setLayoutData(data);
		
		
		/* Initialize Panel */
		panel.setLayout(new FillLayout(SWT.VERTICAL));
		Group htmlGroup = new Group(panel, SWT.NONE);
		htmlGroup.setText("setText");
		htmlText = new Text(htmlGroup, SWT.MULTI);
		htmlButton = new Button(htmlGroup, SWT.PUSH);
		htmlButton.setText("Buscar");
		GridLayout gridLayout = new GridLayout();
		htmlGroup.setLayout(gridLayout);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		htmlText.setLayoutData(gridData);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.END;
		htmlButton.setLayoutData(gridData);
		htmlGroup.layout();
		
		Group scriptGroup = new Group(panel, SWT.NONE);
		scriptGroup.setText("execute");
		scriptText = new Text(scriptGroup, SWT.MULTI);
		scriptButton = new Button(scriptGroup, SWT.PUSH);
		scriptButton.setText("execute");
		gridLayout = new GridLayout();
		scriptGroup.setLayout(gridLayout);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		scriptText.setLayoutData(gridData);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.END;
		scriptButton.setLayoutData(gridData);
		scriptGroup.layout();
		
		browser.setText(html);
		htmlText.setText(html);
		scriptText.setText(script);
		parent.layout();
		
		Listener listener = new Listener() {
			public void handleEvent(Event e) {
				Widget w = e.widget;
				if (w == htmlButton) browser.setText(htmlText.getText());
				if (w == scriptButton) browser.execute(scriptText.getText());
				if(w == searchButton){} //A COMPLETAR CON LA ACCION QUE QUIERAS QUE HAGA EL SEARCHBUTTON
			}
		};
		
		htmlButton.addListener(SWT.Selection, listener);
		scriptButton.addListener(SWT.Selection, listener);
		searchButton.addListener(SWT.Selection, listener);


		
	}
	
}