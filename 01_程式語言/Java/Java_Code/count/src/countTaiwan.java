package count.src;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
// import java.text.*;

//繼承JFrame 表示他就是一個視窗物件
public class countTaiwan extends JFrame implements ActionListener, ItemListener {

	// currency 貨幣單位
	String money_unit_TWD = "TWD";
	String money_unit_UAD = "UAD";
	int frame_width = 1500;
	int frame_height = 900;

	JFrame frame;
	// 一般-散貨
	JLabel lb_BulkCargoCBM, lb_BulkCargoQuality;
	JTextField tf_BulkCargoCBM, tf_BulkCargoQuality;
	JButton bt_BulkCargoSumbit, bt_BulkCargoClear;
	JTextArea jta_BulkCargoResult;

	// 一般-整櫃
	JLabel lb_commonBigContainer, lb_commonSmallContainer;
	JTextField tf_commonBigContainer, tf_commonSmallContainer;
	JButton bt_commonContainerSumbit, bt_commonContainerClear;
	JTextArea jta_commonContainerResult;

	// 一般-散貨櫃
	JLabel lb_commonContainerCBM, lb_commonContainerQuality;
	JTextField tf_commonContainerCBM, tf_commonContainerQuality;

	// 危險櫃
	JLabel lb_dangerBigContainer, lb_dangerSmallContainer;
	JTextField tf_dangerBigContainer, tf_dangerSmallContainer;
	JButton bt_dangerContainerSumbit, bt_dangerContainerClear;
	JTextArea jta_dangerContainerResult;

	// 危險-散貨櫃
	JLabel lb_dangerContainerCBM, lb_dangerContainerQuality;
	JTextField tf_dangerContainerCBM, tf_dangerContainerQuality;

	// 通用
	JLabel lb_commonBulkCargo = new JLabel("一般-散貨");
	JLabel lb_commonContainer = new JLabel("一般-整櫃");
	JLabel lb_dangerContainer = new JLabel("危險櫃");

	// 散貨櫃
	JLabel lb_iscommonBulkContainers, lb_isdangerBulkContainers;
	JCheckBox jc_iscommonBulkContainers, jc_isdangerBulkContainers;

	// 固定費
	JLabel lb_iscommonContainerFixedFee, lb_isdangerContainerFixedFee;
	JCheckBox jc_iscommonContainerFixedFee, jc_isdangerContainerFixedFee;

	/*
	 * OF:海運費 CHF:吊櫃費 BOLPF:提單製作費 SF:封條費 EDF:電放費
	 * CFS:併櫃費 FF:固定費 EDC:出口報關費 EDT:出口報單傳輸費 CYEUF:貨櫃場機具使用費
	 */
	int int_EDC, int_CBM;
	int int_OF_big, int_OF_small, int_CHF_big, int_CHF_small;
	int int_BOLPF, int_SF, int_EDF, int_CFS, int_FF, int_EDT, int_CYEUF;

	public static void main(String[] args) {
		new countTaiwan();
	}

	public countTaiwan() {
		frame = new JFrame("台灣端-報關");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(createConten());
		frame.setSize(frame_width, frame_height);
		centerFrame();
		frame.setVisible(true);
	}

	// 視窗內容
	public Container createConten() {
		Container contenPane = new Container();
		contenPane.setLayout(null);

		Font x = new Font(" ", 0, 14);

		lb_BulkCargoCBM = new JLabel("CBM");
		lb_BulkCargoCBM.setBounds(170, 50, 70, 25);
		tf_BulkCargoCBM = new JTextField("2", 5);
		tf_BulkCargoCBM.setBounds(240, 50, 70, 25);
		lb_BulkCargoQuality = new JLabel("單子數量");
		lb_BulkCargoQuality.setBounds(170, 80, 70, 25);
		tf_BulkCargoQuality = new JTextField("0", 5);
		tf_BulkCargoQuality.setBounds(240, 80, 70, 25);
		bt_BulkCargoSumbit = new JButton("確定");
		bt_BulkCargoSumbit.setBounds(160, 200, 90, 30);
		bt_BulkCargoSumbit.addActionListener(this);
		bt_BulkCargoClear = new JButton("清除");
		bt_BulkCargoClear.setBounds(260, 200, 90, 30);
		bt_BulkCargoClear.addActionListener(this);
		jta_BulkCargoResult = new JTextArea();
		jta_BulkCargoResult.setEditable(false);
		jta_BulkCargoResult.setBounds(10, 250, 490, 600);
		jta_BulkCargoResult.setFont(x);

		lb_commonBigContainer = new JLabel("大櫃");
		lb_commonBigContainer.setBounds(670, 50, 70, 25);
		tf_commonBigContainer = new JTextField("0", 5);
		tf_commonBigContainer.setBounds(740, 50, 70, 25);
		lb_commonSmallContainer = new JLabel("小櫃");
		lb_commonSmallContainer.setBounds(670, 80, 70, 25);
		tf_commonSmallContainer = new JTextField("0", 5);
		tf_commonSmallContainer.setBounds(740, 80, 70, 25);
		jc_iscommonContainerFixedFee = new JCheckBox("有固定費");
		jc_iscommonContainerFixedFee.setBounds(650, 105, 100, 25);
		jc_iscommonBulkContainers = new JCheckBox("是散貨併櫃");
		jc_iscommonBulkContainers.setBounds(750, 105, 100, 25);
		jc_iscommonBulkContainers.addItemListener(this);
		lb_commonContainerCBM = new JLabel("CBM");
		lb_commonContainerCBM.setBounds(670, 130, 70, 25);
		tf_commonContainerCBM = new JTextField("0", 5);
		tf_commonContainerCBM.setBounds(740, 130, 70, 25);
		tf_commonContainerCBM.setEditable(false);
		lb_commonContainerQuality = new JLabel("單子數量");
		lb_commonContainerQuality.setBounds(670, 160, 70, 25);
		tf_commonContainerQuality = new JTextField("0", 5);
		tf_commonContainerQuality.setBounds(740, 160, 70, 25);
		tf_commonContainerQuality.setEditable(false);
		bt_commonContainerSumbit = new JButton("確定");
		bt_commonContainerSumbit.setBounds(660, 200, 90, 30);
		bt_commonContainerSumbit.addActionListener(this);
		bt_commonContainerClear = new JButton("清除");
		bt_commonContainerClear.setBounds(760, 200, 90, 30);
		bt_commonContainerClear.addActionListener(this);
		jta_commonContainerResult = new JTextArea();
		jta_commonContainerResult.setEditable(false);
		jta_commonContainerResult.setBounds(510, 250, 490, 600);
		jta_commonContainerResult.setFont(x);

		lb_dangerBigContainer = new JLabel("大櫃");
		lb_dangerBigContainer.setBounds(1170, 50, 70, 25);
		tf_dangerBigContainer = new JTextField("0", 5);
		tf_dangerBigContainer.setBounds(1240, 50, 70, 25);
		lb_dangerSmallContainer = new JLabel("小櫃");
		lb_dangerSmallContainer.setBounds(1170, 80, 70, 25);
		tf_dangerSmallContainer = new JTextField("0", 5);
		tf_dangerSmallContainer.setBounds(1240, 80, 70, 25);
		jc_isdangerContainerFixedFee = new JCheckBox("有固定費");
		jc_isdangerContainerFixedFee.setBounds(1150, 105, 100, 25);
		jc_isdangerBulkContainers = new JCheckBox("是散貨併櫃");
		jc_isdangerBulkContainers.setBounds(1250, 105, 100, 25);
		jc_isdangerBulkContainers.addItemListener(this);
		lb_dangerContainerCBM = new JLabel("CBM");
		lb_dangerContainerCBM.setBounds(1170, 130, 70, 25);
		tf_dangerContainerCBM = new JTextField("0", 5);
		tf_dangerContainerCBM.setBounds(1240, 130, 70, 25);
		tf_dangerContainerCBM.setEditable(false);
		lb_dangerContainerQuality = new JLabel("單子數量");
		lb_dangerContainerQuality.setBounds(1170, 160, 70, 25);
		tf_dangerContainerQuality = new JTextField("0", 5);
		tf_dangerContainerQuality.setBounds(1240, 160, 70, 25);
		tf_dangerContainerQuality.setEditable(false);
		bt_dangerContainerSumbit = new JButton("確定");
		bt_dangerContainerSumbit.setBounds(1160, 200, 90, 30);
		bt_dangerContainerSumbit.addActionListener(this);
		bt_dangerContainerClear = new JButton("清除");
		bt_dangerContainerClear.setBounds(1260, 200, 90, 30);
		bt_dangerContainerClear.addActionListener(this);
		jta_dangerContainerResult = new JTextArea();
		jta_dangerContainerResult.setEditable(false);
		jta_dangerContainerResult.setBounds(1010, 250, 490, 600);
		jta_dangerContainerResult.setFont(x);

		lb_commonBulkCargo.setBounds(215, 10, 70, 25);
		lb_commonContainer.setBounds(715, 10, 70, 25);
		lb_dangerContainer.setBounds(1215, 10, 70, 25);

		contenPane.add(lb_BulkCargoCBM);
		contenPane.add(tf_BulkCargoCBM);
		contenPane.add(lb_BulkCargoQuality);
		contenPane.add(tf_BulkCargoQuality);
		contenPane.add(bt_BulkCargoSumbit);
		contenPane.add(bt_BulkCargoClear);
		contenPane.add(jta_BulkCargoResult);

		contenPane.add(lb_commonBigContainer);
		contenPane.add(tf_commonBigContainer);
		contenPane.add(lb_commonSmallContainer);
		contenPane.add(tf_commonSmallContainer);
		contenPane.add(jc_iscommonContainerFixedFee);
		contenPane.add(jc_iscommonBulkContainers);
		contenPane.add(lb_commonContainerQuality);
		contenPane.add(tf_commonContainerQuality);
		contenPane.add(lb_commonContainerCBM);
		contenPane.add(tf_commonContainerCBM);
		contenPane.add(bt_commonContainerSumbit);
		contenPane.add(bt_commonContainerClear);
		contenPane.add(jta_commonContainerResult);
		contenPane.add(lb_dangerBigContainer);
		contenPane.add(tf_dangerBigContainer);
		contenPane.add(lb_dangerSmallContainer);
		contenPane.add(tf_dangerSmallContainer);
		contenPane.add(jc_isdangerContainerFixedFee);
		contenPane.add(jc_isdangerBulkContainers);
		contenPane.add(lb_dangerContainerQuality);
		contenPane.add(tf_dangerContainerQuality);
		contenPane.add(lb_dangerContainerCBM);
		contenPane.add(tf_dangerContainerCBM);
		contenPane.add(bt_dangerContainerSumbit);
		contenPane.add(bt_dangerContainerClear);
		contenPane.add(jta_dangerContainerResult);

		contenPane.add(lb_commonBulkCargo);
		contenPane.add(lb_commonContainer);
		contenPane.add(lb_dangerContainer);

		return contenPane;

	}

	// 設定格式
	// private void setUpFormats(){
	// numberFormat = NumberFormat.getNumberInstance();
	// }
	// 螢幕中央
	public void centerFrame() {
		int x, y, screen_width, screen_height;

		// 取得螢幕大小
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		screen_width = screenSize.width;
		screen_height = screenSize.height;

		x = (screen_width - frame_width) / 2;
		y = (screen_height - frame_height) / 2;

		frame.setLocation(x, y);

	}

	public void actionPerformed(ActionEvent ae) {
		JButton button = (JButton) ae.getSource();
		if (button == bt_BulkCargoSumbit) {

			int_CBM = 26;
			int_BOLPF = 550; // 提單製作費
			int_CFS = 380; // 併櫃費(裝櫃費)
			int_CYEUF = 55; // 貨櫃場機具使用費
			int_EDC = 1200; // 出口報關費
			int_EDT = 200; // 出口報單傳輸費

			int cbm = Integer.parseInt(tf_BulkCargoCBM.getText());
			int quality = Integer.parseInt(tf_BulkCargoQuality.getText());

			String s = lb_BulkCargoCBM.getText() + "數量: " + tf_BulkCargoCBM.getText() + "\n";
			s += lb_BulkCargoQuality.getText() + tf_BulkCargoQuality.getText() + "\n";
			s += "=================CBM=================\n";
			s += tf_BulkCargoCBM.getText() + "(CBM數量) * " + int_CBM + "(UAD/1CBM) = "
					+ cbm * int_CBM + " " + money_unit_UAD + "\n";
			s += "=================併櫃費=================\n";
			s += tf_BulkCargoCBM.getText() + "(CBM數量) * " + int_CFS + "(併櫃費) = "
					+ cbm * int_CFS + " " + money_unit_TWD + "\n";
			s += "=================貨櫃場機具使用費=================\n";
			s += tf_BulkCargoCBM.getText() + "(CBM數量) * " + int_CYEUF + "(貨櫃場機具使用費) = "
					+ cbm * int_CYEUF + " " + money_unit_TWD + "\n";
			s += "=================出口報關費-散貨=================\n";
			s += tf_BulkCargoQuality.getText() + "(單子數量) *  " + int_EDC + "(出口報關費-散貨) = "
					+ quality * int_EDC + " " + money_unit_TWD + "\n";
			s += "=================出口報單傳輸費=================\n";
			s += tf_BulkCargoQuality.getText() + "(單子數量) *  " + int_EDT + "(出口報單傳輸費) = "
					+ quality * int_EDT + " " + money_unit_TWD + "\n";
			s += "=================提單製作費=================\n";
			s += tf_BulkCargoQuality.getText() + "(單子數量) *  " + int_BOLPF + "(提單製作費) = "
					+ quality * int_BOLPF + " " + money_unit_TWD + "\n";
			s += "=================總和=================\n";
			s += cbm * int_CBM + " " + money_unit_UAD + " \n"
					+ (cbm * int_CFS + cbm * int_CYEUF + quality * int_EDC + quality * int_EDT
							+ quality * int_BOLPF)
					+ " " + money_unit_TWD;

			jta_BulkCargoResult.setText(s);
		}
		if (button == bt_BulkCargoClear) {
			tf_BulkCargoCBM.setText("2");
			tf_BulkCargoQuality.setText("0");
			jta_BulkCargoResult.setText("");
		}
		if (button == bt_commonContainerSumbit) {
			int_OF_big = 80; // 海運費(大櫃)
			int_OF_small = 40; // 海運費(小櫃)
			int_CHF_big = 7000; // 吊櫃費(大櫃)
			int_CHF_small = 5600; // 吊櫃費(小櫃)
			int_CFS = 180; // 併櫃費(裝櫃費)
			int_SF = 200; // 封條費
			int_EDF = 600; // 電放費
			// 固定費
			if (jc_iscommonContainerFixedFee.isSelected()) {
				int_FF = 1000;
			} else {
				int_FF = 0;
			}
			int_BOLPF = 1600; // 提單製作費
			int_CYEUF = 55; // 貨櫃場機具使用費
			int_EDC = 1200; // 出口報關費
			int_EDT = 200; // 出口報單傳輸費

			int big = Integer.parseInt(tf_commonBigContainer.getText());
			int small = Integer.parseInt(tf_commonSmallContainer.getText());
			int cbm = Integer.parseInt(tf_commonContainerCBM.getText());
			int quality = Integer.parseInt(tf_commonContainerQuality.getText());

			if (jc_iscommonBulkContainers.isSelected()) {
				// 散貨併櫃
				String s = lb_commonBigContainer.getText() + "數量: " + tf_commonBigContainer.getText()
						+ "          |||          ";
				s += lb_commonContainerCBM.getText() + "數量: " + tf_commonContainerCBM.getText() + "\n";
				s += lb_commonSmallContainer.getText() + "數量: " + tf_commonSmallContainer.getText()
						+ "          |||          ";
				s += lb_commonContainerQuality.getText() + ": " + tf_commonContainerQuality.getText()
						+ "\n";
				s += "=================海運費=================\n";
				s += tf_commonBigContainer.getText() + " (大櫃數量) * " + int_OF_big + " (海運費-大櫃) = "
						+ big * int_OF_big + " " + money_unit_UAD + "\n";
				s += tf_commonSmallContainer.getText() + " (小櫃數量) * " + int_OF_small + " (海運費-小櫃) = "
						+ small * int_OF_small + " " + money_unit_UAD + "\n";
				s += "=================吊櫃費=================\n";
				s += tf_commonBigContainer.getText() + " (大櫃數量) * " + int_CHF_big + " (吊櫃費-大櫃) = "
						+ big * int_CHF_big + " " + money_unit_TWD + "\n";
				s += tf_commonSmallContainer.getText() + " (小櫃數量) * " + int_CHF_small + " (吊櫃費-小櫃) = "
						+ small * int_CHF_small + " " + money_unit_TWD + "\n";
				s += "=================提單製作費=================\n";
				s += "(" + tf_commonBigContainer.getText() + " (大櫃數量) + "
						+ tf_commonSmallContainer.getText() + " (小櫃數量)) * "
						+ int_BOLPF + "(提單製作費) = " + ((big + small) * int_BOLPF) + " "
						+ money_unit_TWD + "\n";
				s += "=================封條費=================\n";
				s += "(" + tf_commonBigContainer.getText() + " (大櫃數量) + "
						+ tf_commonSmallContainer.getText() + " (小櫃數量)) * "
						+ int_SF + "(封條費) = " + ((big + small) * int_SF) + " " + money_unit_TWD
						+ "\n";
				s += "=================電放費=================\n";
				s += "(" + tf_commonBigContainer.getText() + " (大櫃數量) + "
						+ tf_commonSmallContainer.getText() + " (小櫃數量)) * "
						+ int_EDF + "(電放費) = " + ((big + small) * int_EDF) + " "
						+ money_unit_TWD + "\n";
				s += "=================併櫃費=================\n";
				s += tf_commonContainerCBM.getText() + " (CBM數量) * " + int_CFS + " (併櫃費) = "
						+ cbm * int_CFS + " " + money_unit_TWD + "\n";
				s += "=================貨櫃場機具使用費=================\n";
				s += tf_commonContainerCBM.getText() + " (CBM數量) * " + int_CYEUF + " (貨櫃場機具使用費) = "
						+ cbm * int_CYEUF + " " + money_unit_TWD + "\n";
				s += "=================出口報關費=================\n";
				s += tf_commonContainerQuality.getText() + " (單子數量) * " + int_EDC + " (出口報關費) = "
						+ quality * int_EDC + " " + money_unit_TWD + "\n";
				s += "=================出口報單傳輸費=================\n";
				s += tf_commonContainerQuality.getText() + " (單子數量) * " + int_EDT + " (出口報單傳輸費) = "
						+ quality * int_EDT + " " + money_unit_TWD + "\n";
				s += "=================固定費=================\n";
				s += int_FF + " " + money_unit_TWD + "\n";
				s += "=================總和=================\n";
				s += (big * int_OF_big + small * int_OF_small) + " " + money_unit_UAD + "\n";
				s += (big * int_CHF_big + small * int_CHF_small + ((big + small) * int_BOLPF)
						+ ((big + small) * int_SF)
						+ ((big + small) * int_EDF) + cbm * int_CFS + cbm * int_CYEUF
						+ quality * int_EDC + quality * int_EDT + int_FF)
						+ " " + money_unit_TWD + "\n";

				jta_commonContainerResult.setText(s);
			} else {
				// 非散貨併櫃
				String s = lb_commonBigContainer.getText() + "數量: " + tf_commonBigContainer.getText()
						+ "\n";
				s += lb_commonSmallContainer.getText() + "數量: " + tf_commonSmallContainer.getText()
						+ "\n";
				s += "=================海運費=================\n";
				s += tf_commonBigContainer.getText() + " (大櫃數量) * " + int_OF_big + " (海運費-大櫃) = "
						+ big * int_OF_big + " " + money_unit_UAD + "\n";
				s += tf_commonSmallContainer.getText() + " (小櫃數量) * " + int_OF_small + " (海運費-小櫃) = "
						+ small * int_OF_small + " " + money_unit_UAD + "\n";
				s += "=================吊櫃費=================\n";
				s += tf_commonBigContainer.getText() + " (大櫃數量) * " + int_CHF_big + " (吊櫃費-大櫃) = "
						+ big * int_CHF_big + " " + money_unit_TWD + "\n";
				s += tf_commonSmallContainer.getText() + " (小櫃數量) * " + int_CHF_small + " (吊櫃費-小櫃) = "
						+ small * int_CHF_small + " " + money_unit_TWD + "\n";
				s += "=================提單製作費=================\n";
				s += "(" + tf_commonBigContainer.getText() + " (大櫃數量) + "
						+ tf_commonSmallContainer.getText() + " (小櫃數量)) * "
						+ int_BOLPF + "(提單製作費) = " + ((big + small) * int_BOLPF) + " "
						+ money_unit_TWD + "\n";
				s += "=================封條費=================\n";
				s += "(" + tf_commonBigContainer.getText() + " (大櫃數量) + "
						+ tf_commonSmallContainer.getText() + " (小櫃數量)) * "
						+ int_SF + "(封條費) = " + ((big + small) * int_SF) + " " + money_unit_TWD
						+ "\n";
				s += "=================電放費=================\n";
				s += "(" + tf_commonBigContainer.getText() + " (大櫃數量) + "
						+ tf_commonSmallContainer.getText() + " (小櫃數量)) * "
						+ int_EDF + "(電放費) = " + ((big + small) * int_EDF) + " "
						+ money_unit_TWD + "\n";
				s += "=================固定費=================\n";
				s += int_FF + " " + money_unit_TWD + "\n";
				s += "=================出口報關費=================\n";
				s += "(" + tf_commonBigContainer.getText() + " (大櫃數量) + "
						+ tf_commonSmallContainer.getText() + " (小櫃數量)) * " + int_EDC
						+ " (出口報關費) = "
						+ ((big + small) * int_EDC) + " " + money_unit_TWD + "\n";
				s += "=================出口報單傳輸費=================\n";
				s += "(" + tf_commonBigContainer.getText() + " (大櫃數量) + "
						+ tf_commonSmallContainer.getText() + " (小櫃數量)) * " + int_EDT
						+ " (出口報單傳輸費) = "
						+ ((big + small) * int_EDT) + " " + money_unit_TWD + "\n";
				s += "=================總和=================\n";
				s += (big * int_OF_big + small * int_OF_small) + " " + money_unit_UAD + "\n";
				s += (big * int_CHF_big + small * int_CHF_small + ((big + small) * int_BOLPF)
						+ ((big + small) * int_SF)
						+ ((big + small) * int_EDF) + cbm * int_CFS + ((big + small) * int_EDC)
						+ ((big + small) * int_EDT) + int_FF)
						+ " " + money_unit_TWD + "\n";

				jta_commonContainerResult.setText(s);
			}

		}
		if (button == bt_commonContainerClear) {
			tf_commonBigContainer.setText("0");
			tf_commonSmallContainer.setText("0");
			tf_commonContainerCBM.setText("0");
			tf_commonContainerQuality.setText("0");
			jta_commonContainerResult.setText("");
		}
		if (button == bt_dangerContainerSumbit) {
			int_OF_big = 210; // 海運費(大櫃)
			int_OF_small = 130; // 海運費(小櫃)
			int_CHF_big = 7000; // 吊櫃費(大櫃)
			int_CHF_small = 5600; // 吊櫃費(小櫃)
			int_CFS = 250; // 併櫃費(裝櫃費)
			int_SF = 200; // 封條費
			int_EDF = 600; // 電放費
			// 固定費
			if (jc_isdangerContainerFixedFee.isSelected()) {
				int_FF = 1000;
			} else {
				int_FF = 0;
			}
			int_BOLPF = 1600; // 提單製作費
			int_CYEUF = 55; // 貨櫃場機具使用費
			int_EDC = 1200; // 出口報關費
			int_EDT = 200; // 出口報單傳輸費

			int big = Integer.parseInt(tf_dangerBigContainer.getText());
			int small = Integer.parseInt(tf_dangerSmallContainer.getText());
			int cbm = Integer.parseInt(tf_dangerContainerCBM.getText());
			int quality = Integer.parseInt(tf_dangerContainerQuality.getText());

			if (jc_isdangerBulkContainers.isSelected()) {
				// 散貨併櫃
				String s = lb_dangerBigContainer.getText() + "數量: " + tf_dangerBigContainer.getText()
						+ "          |||          ";
				s += lb_dangerContainerCBM.getText() + "數量: " + tf_dangerContainerCBM.getText() + "\n";
				s += lb_dangerSmallContainer.getText() + "數量: " + tf_dangerSmallContainer.getText()
						+ "          |||          ";
				s += lb_dangerContainerQuality.getText() + ": " + tf_dangerContainerQuality.getText()
						+ "\n";
				s += "=================海運費=================\n";
				s += tf_dangerBigContainer.getText() + " (大櫃數量) * " + int_OF_big + " (海運費-大櫃) = "
						+ big * int_OF_big + " " + money_unit_UAD + "\n";
				s += tf_dangerSmallContainer.getText() + " (小櫃數量) * " + int_OF_small + " (海運費-小櫃) = "
						+ small * int_OF_small + " " + money_unit_UAD + "\n";
				s += "=================吊櫃費=================\n";
				s += tf_dangerBigContainer.getText() + " (大櫃數量) * " + int_CHF_big + " (吊櫃費-大櫃) = "
						+ big * int_CHF_big + " " + money_unit_TWD + "\n";
				s += tf_dangerSmallContainer.getText() + " (小櫃數量) * " + int_CHF_small + " (吊櫃費-小櫃) = "
						+ small * int_CHF_small + " " + money_unit_TWD + "\n";
				s += "=================提單製作費=================\n";
				s += "(" + tf_dangerBigContainer.getText() + " (大櫃數量) + "
						+ tf_dangerSmallContainer.getText() + " (小櫃數量)) * "
						+ int_BOLPF + "(提單製作費) = " + ((big + small) * int_BOLPF) + " "
						+ money_unit_TWD + "\n";
				s += "=================封條費=================\n";
				s += "(" + tf_dangerBigContainer.getText() + " (大櫃數量) + "
						+ tf_dangerSmallContainer.getText() + " (小櫃數量)) * "
						+ int_SF + "(封條費) = " + ((big + small) * int_SF) + " " + money_unit_TWD
						+ "\n";
				s += "=================電放費=================\n";
				s += "(" + tf_dangerBigContainer.getText() + " (大櫃數量) + "
						+ tf_dangerSmallContainer.getText() + " (小櫃數量)) * "
						+ int_EDF + "(電放費) = " + ((big + small) * int_EDF) + " "
						+ money_unit_TWD + "\n";
				s += "=================併櫃費=================\n";
				s += tf_dangerContainerCBM.getText() + " (CBM數量) * " + int_CFS + " (併櫃費) = "
						+ cbm * int_CFS + " " + money_unit_TWD + "\n";
				s += "=================貨櫃場機具使用費=================\n";
				s += tf_dangerContainerCBM.getText() + " (CBM數量) * " + int_CYEUF + " (貨櫃場機具使用費) = "
						+ cbm * int_CYEUF + " " + money_unit_TWD + "\n";
				s += "=================出口報關費=================\n";
				s += tf_dangerContainerQuality.getText() + " (單子數量) * " + int_EDC + " (出口報關費) = "
						+ quality * int_EDC + " " + money_unit_TWD + "\n";
				s += "=================出口報單傳輸費=================\n";
				s += tf_dangerContainerQuality.getText() + " (單子數量) * " + int_EDT + " (出口報單傳輸費) = "
						+ quality * int_EDT + " " + money_unit_TWD + "\n";
				s += "=================固定費=================\n";
				s += int_FF + " " + money_unit_TWD + "\n";
				s += "=================總和=================\n";
				s += (big * int_OF_big + small * int_OF_small) + " " + money_unit_UAD + "\n";
				s += (big * int_CHF_big + small * int_CHF_small + ((big + small) * int_BOLPF)
						+ ((big + small) * int_SF)
						+ ((big + small) * int_EDF) + cbm * int_CFS + cbm * int_CYEUF
						+ quality * int_EDC + quality * int_EDT + int_FF)
						+ " " + money_unit_TWD + "\n";

				jta_dangerContainerResult.setText(s);
			} else {
				// 非散貨併櫃
				String s = lb_dangerBigContainer.getText() + "數量: " + tf_dangerBigContainer.getText()
						+ "\n";
				s += lb_dangerSmallContainer.getText() + "數量: " + tf_dangerSmallContainer.getText()
						+ "\n";
				s += "=================海運費=================\n";
				s += tf_dangerBigContainer.getText() + " (大櫃數量) * " + int_OF_big + " (海運費-大櫃) = "
						+ big * int_OF_big + " " + money_unit_UAD + "\n";
				s += tf_dangerSmallContainer.getText() + " (小櫃數量) * " + int_OF_small + " (海運費-小櫃) = "
						+ small * int_OF_small + " " + money_unit_UAD + "\n";
				s += "=================吊櫃費=================\n";
				s += tf_dangerBigContainer.getText() + " (大櫃數量) * " + int_CHF_big + " (吊櫃費-大櫃) = "
						+ big * int_CHF_big + " " + money_unit_TWD + "\n";
				s += tf_dangerSmallContainer.getText() + " (小櫃數量) * " + int_CHF_small + " (吊櫃費-小櫃) = "
						+ small * int_CHF_small + " " + money_unit_TWD + "\n";
				s += "=================提單製作費=================\n";
				s += "(" + tf_dangerBigContainer.getText() + " (大櫃數量) + "
						+ tf_dangerSmallContainer.getText() + " (小櫃數量)) * "
						+ int_BOLPF + "(提單製作費) = " + ((big + small) * int_BOLPF) + " "
						+ money_unit_TWD + "\n";
				s += "=================封條費=================\n";
				s += "(" + tf_dangerBigContainer.getText() + " (大櫃數量) + "
						+ tf_dangerSmallContainer.getText() + " (小櫃數量)) * "
						+ int_SF + "(封條費) = " + ((big + small) * int_SF) + " " + money_unit_TWD
						+ "\n";
				s += "=================電放費=================\n";
				s += "(" + tf_dangerBigContainer.getText() + " (大櫃數量) + "
						+ tf_dangerSmallContainer.getText() + " (小櫃數量)) * "
						+ int_EDF + "(電放費) = " + ((big + small) * int_EDF) + " "
						+ money_unit_TWD + "\n";
				s += "=================固定費=================\n";
				s += int_FF + " " + money_unit_TWD + "\n";
				s += "=================出口報關費=================\n";
				s += "(" + tf_dangerBigContainer.getText() + " (大櫃數量) + "
						+ tf_dangerSmallContainer.getText() + " (小櫃數量)) * " + int_EDC
						+ " (出口報關費) = "
						+ ((big + small) * int_EDC) + " " + money_unit_TWD + "\n";
				s += "=================出口報單傳輸費=================\n";
				s += "(" + tf_dangerBigContainer.getText() + " (大櫃數量) + "
						+ tf_dangerSmallContainer.getText() + " (小櫃數量)) * " + int_EDT
						+ " (出口報單傳輸費) = "
						+ ((big + small) * int_EDT) + " " + money_unit_TWD + "\n";
				s += "=================總和=================\n";
				s += (big * int_OF_big + small * int_OF_small) + " " + money_unit_UAD + "\n";
				s += (big * int_CHF_big + small * int_CHF_small + ((big + small) * int_BOLPF)
						+ ((big + small) * int_SF)
						+ ((big + small) * int_EDF) + cbm * int_CFS + ((big + small) * int_EDC)
						+ ((big + small) * int_EDT) + int_FF)
						+ " " + money_unit_TWD + "\n";

				jta_dangerContainerResult.setText(s);
			}
		}
		if (button == bt_dangerContainerClear) {
			tf_dangerBigContainer.setText("0");
			tf_dangerSmallContainer.setText("0");
			tf_dangerContainerCBM.setText("0");
			tf_dangerContainerQuality.setText("0");
			jta_dangerContainerResult.setText("");
		}

	}

	public void itemStateChanged(ItemEvent ie) {
		if (jc_iscommonBulkContainers.isSelected()) {
			tf_commonContainerCBM.setEditable(true);
			tf_commonContainerQuality.setEditable(true);
		} else {
			tf_commonContainerCBM.setEditable(false);
			tf_commonContainerQuality.setEditable(false);
		}
		if (jc_isdangerBulkContainers.isSelected()) {
			tf_dangerContainerCBM.setEditable(true);
			tf_dangerContainerQuality.setEditable(true);
		} else {
			tf_dangerContainerCBM.setEditable(false);
			tf_dangerContainerQuality.setEditable(false);
		}
	}

}
