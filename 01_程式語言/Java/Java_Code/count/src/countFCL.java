package count.src;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class countFCL implements ActionListener {

	int THC_big, THC_small, EBS_big, EBS_small, DOC; // THC:吊櫃費 EBS:燃料附加費 DOC:文件費
	int CF, DGF; // CF:綜合費 DGF:危品費

	int frame_width = 500;
	int frame_height = 600;

	String money;
	JFrame frameFCL;
	JLabel lb_big, lb_small, lb_BLFCL;
	JTextField tf_big, tf_small, tf_BLFCL;
	JButton bt_sumbitFCL, bt_cancelFCL;
	JTextArea jta;

	public countFCL() {
		frameFCL = new JFrame("大陸端-整櫃");
		frameFCL.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameFCL.setContentPane(createContenPaneFCL());
		frameFCL.setSize(frame_width, frame_height);
		centerFrame();
		frameFCL.setVisible(true);
	}

	public Container createContenPaneFCL() {
		Container contenPaneFCL = new Container();
		contenPaneFCL.setLayout(null);
		jta = new JTextArea(11, 50); // 列數為11 欄數為50
		jta.setEditable(false); // 不可修改狀態

		lb_big = new JLabel("大櫃");
		tf_big = new JTextField("0", 3);
		lb_small = new JLabel("小櫃");
		tf_small = new JTextField("0", 3);
		lb_BLFCL = new JLabel("BL");
		tf_BLFCL = new JTextField("1", 3);
		tf_BLFCL.setEditable(false);

		bt_sumbitFCL = new JButton("確定");
		bt_cancelFCL = new JButton("清空");
		bt_sumbitFCL.addActionListener(this);
		bt_cancelFCL.addActionListener(this);

		contenPaneFCL.add(lb_big);
		contenPaneFCL.add(tf_big);
		contenPaneFCL.add(lb_small);
		contenPaneFCL.add(tf_small);
		contenPaneFCL.add(lb_BLFCL);
		contenPaneFCL.add(tf_BLFCL);
		contenPaneFCL.add(bt_sumbitFCL);
		contenPaneFCL.add(bt_cancelFCL);
		contenPaneFCL.add(jta);

		jta.setBounds(50, 160, 400, 380);

		lb_big.setBounds(150, 25, 30, 25);
		tf_big.setBounds(265, 25, 30, 25);
		lb_small.setBounds(150, 50, 30, 25);
		tf_small.setBounds(265, 50, 30, 25);
		lb_BLFCL.setBounds(150, 75, 30, 25);
		tf_BLFCL.setBounds(265, 75, 30, 25);

		bt_sumbitFCL.setBounds(130, 110, 90, 30);
		bt_cancelFCL.setBounds(300, 110, 90, 30);

		return contenPaneFCL;
	}

	public void actionPerformed(ActionEvent ae) {
		JButton button = (JButton) ae.getSource();
		if (button == bt_sumbitFCL) {
			// 價錢
			THC_big = 1230;
			THC_small = 825;
			EBS_big = 2000;
			EBS_small = 1000;
			DOC = 510;
			CF = 30;
			DGF = 300;
			money = "RMB";

			int a, b, c;
			a = Integer.parseInt(tf_big.getText());
			b = Integer.parseInt(tf_small.getText());
			c = Integer.parseInt(tf_BLFCL.getText());

			String s = lb_big.getText() + "數量:" + tf_big.getText() + "\n";
			s += lb_small.getText() + "數量:" + tf_small.getText() + "\n";
			s += lb_BLFCL.getText() + "數量:" + tf_BLFCL.getText() + "\n";
			s += "貨幣:" + money + "\n";
			s += "==================普通櫃部分==================\n";
			s += "吊櫃費:\n";
			s += lb_big.getText() + "數量: " + tf_big.getText() + " * " + "大櫃價錢:" + THC_big + " = " + a * THC_big + "\n";
			s += lb_small.getText() + "數量: " + tf_small.getText() + " * " + "小櫃價錢:" + THC_small + " = " + b * THC_small
					+ "\n";
			s += "燃油附加費:\n";
			s += lb_big.getText() + "數量: " + tf_big.getText() + " * " + "大櫃價錢:" + EBS_big + " = " + a * EBS_big + "\n";
			s += lb_small.getText() + "數量: " + tf_small.getText() + " * " + "小櫃價錢:" + EBS_small + " = " + b * EBS_small
					+ "\n";
			s += "文件費:\n";
			s += lb_BLFCL.getText() + "數量: " + tf_BLFCL.getText() + " * " + DOC + " = " + c * DOC + "\n";
			s += "==================危險櫃部分==================\n";
			s += "綜合費:\n";
			s += lb_BLFCL.getText() + "數量:" + tf_BLFCL.getText() + " * " + CF + " = " + c * CF + "\n";
			s += "危品費\n";
			s += lb_BLFCL.getText() + "數量:" + tf_BLFCL.getText() + " * " + DGF + " = " + c * DGF + "\n";
			s += "=====================總和=====================\n";
			s += "普通櫃總和: " + (a * THC_big + b * THC_small + a * EBS_big + b * EBS_small + c * DOC) + " (" + money
					+ ")\n";
			s += "危險櫃總和: " + (a * THC_big + b * THC_small + a * EBS_big + b * EBS_small + c * DOC + c * CF + c * DGF)
					+ " (" + money + ") ";
			jta.setText(s);
		}
		if (button == bt_cancelFCL) {
			tf_big.setText("0");
			tf_small.setText("0");
			tf_BLFCL.setText("1");
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

		frameFCL.setLocation(x, y);
	}

	public static void main(String[] args) {
		new countFCL();
	}
}
