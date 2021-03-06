package com.example.main;

import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.example.base.Tag;
import com.example.base.Token;
import com.example.base.Word;
import com.example.lexer.Lexer;
import com.example.tool.FileHelper;

public class Main extends Frame {
	
	public static Token[] tok = new Token[600000];
	public static int[] line = new int[600000];
	public static final int x = 200,y = 30,width = 900,height = 600;
	public static int length = 0;
	public static JTextArea output = new JTextArea();
	public static JTextArea input = new JTextArea();
	public static String FILE_INPUT = "file/test1.txt";
	
	public static String FILE_OUTPUT = "first\\file\\output.txt";
	public Main() {
		JPanel panel1 = new JPanel();
		addInput(panel1);
		JPanel panel2 = new JPanel();
		addOutput(panel2);
		
		this.add(panel1);
		this.add(panel2);

	}

    public static void main(String[] args) throws IOException {
//    	new Main().lanchar();
//		getInputFromText();
//		write();
//		output();
//		outputToFile();
//		String[] str = FileHelper.getInputFromText();
//		for (int i = 0; i < str.length; i++) {
//			System.out.println(str[i]);
//		}
//		System.out.println(getInputFromText());
		getSLR1Input();
    }

    public static String getInputFromText() {
		StringBuilder builder = new StringBuilder("");
		try {
			String encoding = "GBK";
			File file = new File(FILE_INPUT);
			if (file.isFile() && file.exists()) {
				InputStreamReader reader = new InputStreamReader(new FileInputStream(file), encoding);
				BufferedReader bufferedReader = new BufferedReader(reader);
				String input = "";
				while ((input = bufferedReader.readLine()) != null) {
					builder.append(input).append("\n");
				}
			} else {
				System.out.println("�Ҳ���ָ���ļ���");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return builder.toString();
	}

	public static void outputToFile() throws IOException {

		File file = new File(FILE_OUTPUT);
		OutputStream outputStream = new FileOutputStream(file);

		StringBuilder builder = new StringBuilder("");
		for(int i = 0; i < length; i++) {
			if(tok[i] != null) {
				if(tok[i].tag == 1000) {
					builder.append("<ID,"+tok[i].toString()+">").append("\r").append("\n");
				}
				else if(tok[i].tag == 2000) {
					builder.append("<NUM,"+tok[i].toString()+">").append("\r").append("\n");
				}
				else if(tok[i].tag == 3000) {
					builder.append("<String,"+tok[i].toString()+">").append("\r").append("\n");
				}
				else if(tok[i].tag == 0) {
					builder.append("<ERROR,on "+line[i]+" line>").append("\r").append("\n");
				}
				else if(tok[i].tag == 5000) {
					builder.append("<REAL,"+tok[i].toString()+">").append("\r").append("\n");
				}
				else {
					builder.append("<"+tok[i].toString()+">").append("\r").append("\n");
				}
			}
		}

		builder.append("\r").append("\n");

		Iterator it = Lexer.words.keySet().iterator();
		Iterator va = Lexer.words.values().iterator();
		while (va.hasNext()&&it.hasNext()) {
			Word value =(Word)va.next();
			int key = value.tag;

			if(key == Tag.ID) {
				builder.append("<ID,"+value.lexeme+">").append("\r").append("\n");
			}
			else if( key == Tag.NUM) {
				builder.append("<NUM,"+value.lexeme+">").append("\r").append("\n");
			}
			else if(key == Tag.STRING) {
				builder.append("<STRING,"+value.lexeme+">").append("\r").append("\n");
			}
			else if(key == Tag.REAL) {
				builder.append("<REAL,"+value.lexeme+">").append("\r").append("\n");
			}
			else {
				builder.append("<"+value.lexeme+">").append("\r").append("\n");
			}

		}

		byte[] data = builder.toString().getBytes();
		outputStream.write(data);
		outputStream.close();
	}

	public static void write()  {

    	Lexer lex = new Lexer();
    	length = 0;
    	for(int i = 0; ;i++) {
    		tok[i] = lex.scan();
			if(tok[i].tag == 40000) {
				break;
			}
    		line[i] = lex.line;
    		length++;
    	}
    }

    public static String[] getSLR1Input() {
		write();
		ArrayList<String> input = new ArrayList<>();
		for (int i = 0; tok[i].tag != 40000; i++) {
			switch (tok[i].tag) {
				case Tag.NUM:
					input.add("num");
					break;
				case Tag.ID:
					input.add("id");
					break;
				default:
					input.add(tok[i].toString().toLowerCase());
					break;
			}
		}
		input.add("$");

		return (String[]) input.toArray(new String[input.size()]);
	}
    	/**
    	 * 输出词法分析结果
    	 */
    public static void output() {
    	for(int i = 0; i < length; i++) {
    		if(tok[i] != null) {
    			if(tok[i].tag == 1000) {
					System.out.println("<ID,"+tok[i].toString()+">");
    			}
    			else if(tok[i].tag == 2000) {
					System.out.println("<NUM,"+tok[i].toString()+">");
    			}
    			else if(tok[i].tag == 3000) {
					System.out.println("<String,"+tok[i].toString()+">");
    			}
				else if(tok[i].tag == 5000) {
					System.out.println("<REAL,"+tok[i].toString()+">");
				}
    			else if(tok[i].tag == 0) {
					System.out.println("<ERROR,on "+line[i]+" line>");
    			}else {
					System.out.println("<"+tok[i].toString()+">");
    			}
    		}
    	}

    	/*System.out.println();

		Iterator it = Lexer.words.keySet().iterator();
		Iterator va = Lexer.words.values().iterator();
		while (va.hasNext()&&it.hasNext()) {
			Word value =(Word)va.next();
			int key = value.tag;

			if(key == 1000) {
				System.out.println("<ID,"+value.lexeme+">");
			}
			else if( key == 2000) {
				System.out.println("<NUM,"+value.lexeme+">");
			}
			else if(key == 3000) {
				System.out.println("<String,"+value.lexeme+">");
			}
			else if(key == 5000) {
				System.out.println("<Real,"+value.lexeme+">");
			}
			else {
				System.out.println("<"+value.lexeme+">");
			}
		}*/
	}

	private void addInput(JPanel panel1) {
		// TODO Auto-generated method stub
		panel1.setBounds(30, 60, 840 ,280);//220
//		GridLayout Layout = new GridLayout(3,3);
		panel1.setLayout(null);

		input.setText(null);
		input.setLineWrap(true);
		input.setBounds(0, 0, 840, 280);
		panel1.add(input);
		input.setBackground(new Color(211,211,211));

	}

	private void addOutput(JPanel panel2) {
		panel2.setBounds(30, 360, 840 ,220);
		panel2.setLayout(null);

		panel2.add(output);
		output.setBounds(0, 0, 840, 220);
		output.setBackground(Color.gray);

	}

	public void lanchar(){

		this.setBounds(x, y, width, height);
		this.setTitle("PASCAL编译�?");
		this.setBackground(Color.white);
		this.setResizable(false);
		this.setLayout(null);


		//在窗体事件源上添加带有处理事件的监听器
		this.addWindowListener(new WindowAdapter(){

			public void windowClosing(WindowEvent e){
				System.exit(0);//关闭窗口处理关闭动作监听事件
			}
		});


		/**
		 * 添加按钮监听
		 */
		Button b=new Button("编译");//在窗口中添加�?个按钮；
		b.setBounds(30, 30, 40, 20);
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				output();
			}
		});
		this.add(b);//将按钮添加到窗口�?

		this.setVisible(true);

	}
}
