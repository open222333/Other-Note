package count.src;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class countChina extends JFrame implements ActionListener, ItemListener {

	JFrame frame;
	int frame_width = 800;
	int frame_height = 500;

	/*
	 * GUIComponent 當作 ArrayList 的參考變數，使用 JComponent 當作 ArrayList 元素的儲存型態，
	 * 這是因為 JComponent 為 JLabel 、 JTextField 、 JButton 的父類別 (superclass) 。
	 * 父類別可作為子類別 (subclass) 的通用類別，這也是物件導向程式設計多型 (polymorphism) 的基礎之一。
	 */
	// 填滿 BOTH:垂直水平都填滿 VETICAL:垂直填滿 HORIZONTAL:水平填滿 NONE:不填滿
	int fill[] = {
			GridBagConstraints.BOTH,
			GridBagConstraints.VERTICAL,
			GridBagConstraints.HORIZONTAL,
			GridBagConstraints.NONE
	};

	/*
	 * 對齊
	 * CENTER:中央對齊 EAST:向右對齊 SOUTHEAST:右下對齊 SOUTH:向下對齊 SOUTHWEST:左下對齊
	 * WEST:向左對齊 NORTHWEST:左上對齊 NORTH:向上對齊 NORTHEAST:右上對齊
	 */
	int[] anchor = {
			GridBagConstraints.CENTER,
			GridBagConstraints.EAST,
			GridBagConstraints.SOUTHEAST,
			GridBagConstraints.SOUTH,
			GridBagConstraints.SOUTHWEST,
			GridBagConstraints.WEST,
			GridBagConstraints.NORTHWEST,
			GridBagConstraints.NORTH,
			GridBagConstraints.NORTHEAST, };

	int att[][] = {
			{ 0, 0, 1, 1, 0, 0, fill[3], anchor[5] }, // 整櫃 CheckBox 1
			{ 1, 1, 1, 1, 0, 0, fill[3], anchor[0] }, // 大櫃 Label 2
			{ 2, 1, 2, 1, 0, 0, fill[3], anchor[0] }, // 大櫃 TextField 3
			{ 1, 2, 1, 1, 0, 0, fill[3], anchor[0] }, // 小櫃 Label 4
			{ 2, 2, 2, 1, 0, 0, fill[3], anchor[0] }, // 小櫃 TextField 5
			{ 0, 3, 1, 1, 0, 0, fill[3], anchor[5] }, // 散貨 CheckBox 6
			{ 1, 4, 1, 1, 0, 0, fill[3], anchor[0] }, // 材(CBM) Label 7
			{ 2, 4, 2, 1, 0, 0, fill[3], anchor[0] }, // 材(CBM) TextField 8
			{ 1, 5, 1, 1, 0, 0, fill[3], anchor[0] }, // 提單 Label 9
			{ 2, 5, 2, 1, 0, 0, fill[3], anchor[0] }, // 提單 TextField 10
			{ 0, 6, 1, 1, 0, 0, fill[3], anchor[5] }, // 代理進口費 CheckBox 11
			{ 1, 7, 1, 1, 0, 0, fill[3], anchor[0] }, // 代理進口費 Label 12
			{ 2, 7, 2, 1, 0, 0, fill[3], anchor[0] }, // 代理進口費 TextField 13
			{ 1, 8, 1, 1, 0, 0, fill[3], anchor[0] }, // 貨值1% CheckBox 14
			{ 1, 9, 1, 1, 0, 0, fill[3], anchor[0] }, // 貨值1% Label 15
			{ 2, 9, 2, 1, 0, 0, fill[3], anchor[0] }, // 貨值1% TextField 16
			{ 0, 10, 1, 1, 0, 0, fill[3], anchor[5] }, // 查驗費 CheckBox 17
			{ 1, 11, 1, 1, 0, 0, fill[3], anchor[0] }, // 查驗費 Label 18
			{ 2, 11, 2, 1, 0, 0, fill[3], anchor[0] }, // 查驗費 TextField 19
			{ 1, 13, 1, 1, 0, 0, fill[3], anchor[0] }, // 確認 Button 20
			{ 3, 13, 1, 1, 0, 0, fill[3], anchor[0] }, // 清除 Button 21
			{ 5, 0, 25, 14, 0, 0, fill[0], anchor[0] }, // 顯示 22

			// 空白-0欄
			{ 0, 1, 1, 2, 0, 0, fill[3], anchor[0] }, // 23
			{ 0, 4, 1, 2, 0, 0, fill[3], anchor[0] }, // 24
			{ 0, 7, 1, 3, 0, 0, fill[3], anchor[0] }, // 25
			{ 0, 11, 1, 4, 0, 0, fill[3], anchor[0] }, // 26
			// 空白-中間欄
			{ 1, 0, 3, 1, 0, 0, fill[3], anchor[0] }, // 27
			{ 1, 3, 3, 1, 0, 0, fill[3], anchor[0] }, // 28
			{ 1, 6, 3, 1, 0, 0, fill[3], anchor[0] }, // 29
			{ 2, 8, 2, 1, 0, 0, fill[3], anchor[0] }, // 30
			{ 1, 10, 3, 1, 0, 0, fill[3], anchor[0] }, // 31
			{ 1, 12, 3, 1, 0, 0, fill[3], anchor[0] }, // 32
			{ 2, 13, 1, 1, 0, 0, fill[3], anchor[0] }, // 33
			{ 1, 14, 3, 1, 0, 0, fill[3], anchor[0] }, // 34
			// 空白-4欄
			{ 4, 0, 1, 14, 0, 0, fill[3], anchor[0] } // 35
	};

	// 輸入區塊
	JLabel lb_BigContainer, lb_SmallContainer, lb_BulkCargoCBM, lb_BillQuantity, lb_IAF, lb_IF, lb_Value;

	JTextField tf_BigContainer, tf_SmallContainer, tf_BulkCargoCBM, tf_BillQuantity, tf_IAF, tf_IF, tf_Value;

	JCheckBox cb_Container, cb_BulkCargo, cb_IAF, cb_IF, cb_IAFonePercent;

	JButton bt_submit, bt_cancel;

	JTextArea jta_view;

	/*
	 * 所有費用:
	 * 吊櫃費: CHF 文件費:DOLPF 燃油附加費:FS 碼頭存放費:DSC
	 * 碼頭換七聯單費:DFS 提還進場卸貨櫃費:DWAC
	 * 報關報檢費:CCF 代理進口費:IAF 危險櫃派送費:CDDF
	 *
	 * 換單費:FASF 輸單費:LASF 拆箱費:UF 包幹費:LSF 機檢費:MIF 港建費:PCF
	 * 集司碼頭換七聯單費:SDPFS
	 *
	 * 查驗費:IF
	 */
	public countChina() {
		frame = new JFrame("大陸端-報價單");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridBagLayout());
		frame.setSize(frame_width, frame_height);
		frame.setContentPane(createContainer());
		centerFrame();

		frame.setVisible(true);
	}

	public Container createContainer() {
		Container container = new Container();
		container.setLayout(new GridBagLayout());

		cb_Container = new JCheckBox("整櫃");
		cb_Container.addItemListener(this);
		container.add(cb_Container, addComponent(0));

		lb_BigContainer = new JLabel("大櫃 :");
		container.add(lb_BigContainer, addComponent(1));

		tf_BigContainer = new JTextField("0", 5);
		tf_BigContainer.setEditable(false);
		container.add(tf_BigContainer, addComponent(2));

		lb_SmallContainer = new JLabel("小櫃 :");
		container.add(lb_SmallContainer, addComponent(3));

		tf_SmallContainer = new JTextField("0", 5);
		tf_SmallContainer.setEditable(false);
		container.add(tf_SmallContainer, addComponent(4));

		cb_BulkCargo = new JCheckBox("散貨 :");
		cb_BulkCargo.addItemListener(this);
		container.add(cb_BulkCargo, addComponent(5));

		lb_BulkCargoCBM = new JLabel("材(CBM) :");
		container.add(lb_BulkCargoCBM, addComponent(6));

		tf_BulkCargoCBM = new JTextField("2", 5);
		tf_BulkCargoCBM.setEditable(false);
		container.add(tf_BulkCargoCBM, addComponent(7));

		lb_BillQuantity = new JLabel("提單 :");
		container.add(lb_BillQuantity, addComponent(8));

		tf_BillQuantity = new JTextField("1", 5);
		tf_BillQuantity.setEditable(false);
		container.add(tf_BillQuantity, addComponent(9));

		cb_IAF = new JCheckBox("更改代理進口費 :");
		cb_IAF.addItemListener(this);
		cb_IAF.setEnabled(true);
		container.add(cb_IAF, addComponent(10));

		lb_IAF = new JLabel("代理進口費 :");
		container.add(lb_IAF, addComponent(11));

		tf_IAF = new JTextField("1000", 5);
		tf_IAF.setEditable(false);
		container.add(tf_IAF, addComponent(12));

		cb_IAFonePercent = new JCheckBox("按1%貨值計算");
		cb_IAFonePercent.addItemListener(this);
		cb_IAFonePercent.setEnabled(false);
		container.add(cb_IAFonePercent, addComponent(13));

		lb_Value = new JLabel("貨值 :");
		container.add(lb_Value, addComponent(14));

		tf_Value = new JTextField("0", 5);
		tf_Value.setEditable(false);
		container.add(tf_Value, addComponent(15));

		cb_IF = new JCheckBox("是否有查驗");
		cb_IF.addItemListener(this);
		cb_IF.setEnabled(true);
		container.add(cb_IF, addComponent(16));

		lb_IF = new JLabel("查驗費 :");
		container.add(lb_IF, addComponent(17));

		tf_IF = new JTextField("0", 5);
		tf_IF.setEditable(false);
		container.add(tf_IF, addComponent(18));

		bt_submit = new JButton("確 定");
		bt_submit.addActionListener(this);
		container.add(bt_submit, addComponent(19));

		bt_cancel = new JButton("重 設");
		bt_cancel.addActionListener(this);
		container.add(bt_cancel, addComponent(20));

		jta_view = new JTextArea(" ", 20, 25);
		jta_view.setEditable(false);
		container.add(jta_view, addComponent(21));

		JPanel jp_null = new JPanel();
		container.add(jp_null, addComponent(22));
		container.add(jp_null, addComponent(23));
		container.add(jp_null, addComponent(24));
		container.add(jp_null, addComponent(25));
		container.add(jp_null, addComponent(26));
		container.add(jp_null, addComponent(27));
		container.add(jp_null, addComponent(28));
		container.add(jp_null, addComponent(29));
		container.add(jp_null, addComponent(30));
		container.add(jp_null, addComponent(31));
		container.add(jp_null, addComponent(32));
		container.add(jp_null, addComponent(33));
		container.add(jp_null, addComponent(34));

		return container;
	}

	// 主程式
	public static void main(String[] args) {
		new countChina();
	}

	public GridBagConstraints addComponent(int i) {
		GridBagConstraints c = new GridBagConstraints();
		int a[] = att[i];

		c.gridx = a[0];
		c.gridy = a[1];
		c.gridwidth = a[2];
		c.gridheight = a[3];
		c.weightx = a[4];
		c.weighty = a[5];
		c.fill = a[6];
		c.anchor = a[7];
		c.insets = new Insets(5, 5, 5, 5);
		return c;
	}

	// 使螢幕在中央
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

	// 按鍵
	@Override
	public void actionPerformed(ActionEvent ae) {
		JButton button = (JButton) ae.getSource();
		if (button == bt_submit) {
			if (cb_Container.isSelected()) {

			}
			if (cb_BulkCargo.isSelected()) {

			}
		}
		if (button == bt_cancel) {
			tf_BigContainer.setText("0");
			tf_SmallContainer.setText("0");
			tf_BulkCargoCBM.setText("2");
			tf_BillQuantity.setText("1");
			tf_IAF.setText("1000");
			tf_Value.setText("0");
			tf_IF.setText("0");
			jta_view.setText(" ");
		}
	}

	// 勾選
	@Override
	public void itemStateChanged(ItemEvent ie) {
		// 整櫃
		if (cb_Container.isSelected()) {
			tf_BigContainer.setEditable(true);
			tf_SmallContainer.setEditable(true);
			cb_BulkCargo.setEnabled(false);
		} else {
			tf_BigContainer.setEditable(false);
			tf_SmallContainer.setEditable(false);
			cb_BulkCargo.setEnabled(true);
		}

		// 散貨
		if (cb_BulkCargo.isSelected()) {
			tf_BulkCargoCBM.setEditable(true);
			tf_BillQuantity.setEditable(true);
			cb_Container.setEnabled(false);
		} else {
			tf_BulkCargoCBM.setEditable(false);
			tf_BillQuantity.setEditable(false);
			cb_Container.setEnabled(true);
		}

		// 代理進口費
		if (cb_IAF.isSelected()) {
			tf_IAF.setEditable(true);
			cb_IAFonePercent.setEnabled(true);
			if (cb_IAFonePercent.isSelected()) {
				tf_Value.setEditable(true);
			} else {
				tf_Value.setEditable(false);
			}
		} else {
			tf_IAF.setEditable(false);
			cb_IAFonePercent.setEnabled(false);
		}

		// 查驗
		if (cb_IF.isSelected()) {
			tf_IF.setEditable(true);
		} else {
			tf_IF.setEditable(false);
		}
	}
}
