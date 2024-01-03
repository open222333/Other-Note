package count.src;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class countLCL implements ActionListener {

	// UF:拆箱費 EF:換單費 IDF:輸單費 MIF:機檢費 LS:包干費 TF:理貨費 PCF:港口建設費
	int UF_RT, EF_BL, IDF_BL, MIF_JOB, LS_JOB, TF_BL, PCF_JOB;
	// 視窗大小
	int frame_width = 500;
	int frame_height = 700;
	String money;
	JFrame frameLCL;
	JLabel lb_BLch, lb_JOBch, lb_RTch;
	JLabel lb_RT, lb_JOB, lb_BLLCL;
	JTextField tf_RT, tf_JOB, tf_BLLCL;
	JButton bt_sumbitLCL, bt_cancelLCL;
	JTextArea jta;

	public countLCL() {
		frameLCL = new JFrame("大陸端-散貨");
		frameLCL.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameLCL.setContentPane(createContenPaneLCL());
		frameLCL.setSize(frame_width, frame_height);
		centerFrame();
		frameLCL.setVisible(true);
	}

	public Container createContenPaneLCL() {
		Container contenPaneLCL = new Container();
		contenPaneLCL.setLayout(null);
		jta = new JTextArea(11, 50); // 列數為11 欄數為50
		jta.setEditable(false); // 不可修改狀態

		lb_RT = new JLabel("RT");
		tf_RT = new JTextField("0", 3);
		lb_JOB = new JLabel("JOB");
		tf_JOB = new JTextField("0", 3);
		lb_BLLCL = new JLabel("BL");
		tf_BLLCL = new JTextField("0", 3);

		lb_RTch = new JLabel("(1 CBM)");
		lb_BLch = new JLabel("(大提單)");
		lb_JOBch = new JLabel("(SO收費)");

		bt_sumbitLCL = new JButton("確定");
		bt_cancelLCL = new JButton("清空");
		bt_sumbitLCL.addActionListener(this);
		bt_cancelLCL.addActionListener(this);

		contenPaneLCL.add(lb_RT);
		contenPaneLCL.add(tf_RT);
		contenPaneLCL.add(lb_JOB);
		contenPaneLCL.add(tf_JOB);
		contenPaneLCL.add(lb_BLLCL);
		contenPaneLCL.add(tf_BLLCL);
		contenPaneLCL.add(bt_sumbitLCL);
		contenPaneLCL.add(bt_cancelLCL);
		contenPaneLCL.add(lb_RTch);
		contenPaneLCL.add(lb_JOBch);
		contenPaneLCL.add(lb_BLch);
		contenPaneLCL.add(jta);

		jta.setBounds(50, 200, 400, 400);

		lb_RT.setBounds(110, 25, 30, 25);
		lb_RTch.setBounds(150, 25, 70, 25);
		tf_RT.setBounds(265, 25, 30, 25);
		lb_JOB.setBounds(110, 50, 30, 25);
		lb_JOBch.setBounds(150, 50, 70, 25);
		tf_JOB.setBounds(265, 50, 30, 25);
		lb_BLLCL.setBounds(110, 75, 30, 25);
		lb_BLch.setBounds(150, 75, 70, 25);
		tf_BLLCL.setBounds(265, 75, 30, 25);

		bt_sumbitLCL.setBounds(130, 150, 90, 30);
		bt_cancelLCL.setBounds(300, 150, 90, 30);

		return contenPaneLCL;
	}

	public void actionPerformed(ActionEvent ae) {
		JButton button = (JButton) ae.getSource();
		if (button == bt_sumbitLCL) {
			UF_RT = 57;
			EF_BL = 150;
			IDF_BL = 15;
			MIF_JOB = 100;
			LS_JOB = 200;
			TF_BL = 20;
			PCF_JOB = 35;
			money = "RMB";

			int a, b, c;
			a = Integer.parseInt(tf_RT.getText());
			b = Integer.parseInt(tf_JOB.getText());
			c = Integer.parseInt(tf_BLLCL.getText());

			String s = lb_RT.getText() + "數量:" + tf_RT.getText() + "\n";
			s += lb_JOB.getText() + "數量:" + tf_JOB.getText() + "\n";
			s += lb_BLLCL.getText() + "數量:" + tf_BLLCL.getText() + "\n";
			s += "貨幣:" + money + "\n";
			s += "==================================" + "\n";
			s += "廣州拆箱費:" + "\n";
			s += "價錢: " + UF_RT + " * " + "RT數量: " + tf_RT.getText() + " = " + a * UF_RT + "\n";
			s += "廣州換單費:" + "\n";
			s += "價錢: " + EF_BL + " * " + "BL數量: " + tf_BLLCL.getText() + " = " + c * EF_BL + "\n";
			s += "廣州輸單費:" + "\n";
			s += "價錢: " + IDF_BL + " * " + "BL數量: " + tf_BLLCL.getText() + " = " + c * IDF_BL + "\n";
			s += "機檢費:" + "\n";
			s += "價錢: " + MIF_JOB + " * " + "JOB數量: " + tf_JOB.getText() + " = " + b * MIF_JOB + "\n";
			s += "包干費:" + "\n";
			s += "價錢: " + LS_JOB + " * " + "JOB數量: " + tf_JOB.getText() + " = " + b * LS_JOB + "\n";
			s += "理貨費:" + "\n";
			s += "價錢: " + TF_BL + " * " + "BL數量: " + tf_BLLCL.getText() + " = " + c * TF_BL + "\n";
			s += "港口建設費:" + "\n";
			s += "價錢: " + PCF_JOB + " * " + "JOB數量: " + tf_JOB.getText() + " = " + b * PCF_JOB + "\n";
			s += "==================================" + "\n";
			s += "總和: " + (a * UF_RT + c * EF_BL + c * IDF_BL + b * MIF_JOB + b * LS_JOB + c * TF_BL + b * PCF_JOB)
					+ " (" + money + ") ";
			jta.setText(s);
		}
		if (button == bt_cancelLCL) {
			tf_RT.setText("0");
			tf_JOB.setText("0");
			tf_BLLCL.setText("0");
			jta.setText("");
		}
	}

	public void centerFrame() {
		int x, y, screen_width, screen_height;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // 取得螢幕大小

		screen_width = screenSize.width;
		screen_height = screenSize.height;

		x = (screen_width - frame_width) / 2;
		y = (screen_height - frame_height) / 2;

		frameLCL.setLocation(x, y);
	}

	public static void main(String[] args) {
		new countLCL();
	}

}
