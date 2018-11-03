import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class ShowMemory extends JPanel implements ActionListener {
    private static Font defaultFont = new Font("SimSun", Font.PLAIN, 12); //默认字体
    private Vector<Vector<Object>> data = new Vector<Vector<Object>>();
    private JTable table;  //表格
    private MyTableModel model=new MyTableModel();  //声明模型对象
    private int beginNumber=0;
    private JButton createButton,deleteButton,exitButton;
    private Model block=new Model(beginNumber,1024,0,false);
    private List<Model> blockList=new ArrayList<>();
    private JProgressBar progressBar;
    private JLabel jLabel2;
    private JLabel jLabel4;
    public ShowMemory(){
        initBlockList();
        initView();
        updateData();
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==createButton){
            createProcess();
        }else if(e.getSource()==deleteButton){
            int name = Integer.parseInt(JOptionPane.showInputDialog("请输入要撤销的进程编号："));
            deleteProcess(name);
        }else if(e.getSource()==exitButton){
            System.exit(0);
        }
    }
    private void initView(){
        Box baseBox = Box.createVerticalBox();     //根盒子
        baseBox.setSize(5000, 200);
        //-------------------容器内容------------------------------
        JPanel showPanel = new JPanel();
        Box vtemp = Box.createVerticalBox();
        Box htemp1 = Box.createHorizontalBox();
        Box htemp2 = Box.createHorizontalBox();
        Box vtemp1 = Box.createVerticalBox();
        Box vtemp2 = Box.createVerticalBox();
        //第一列水平布局
        //左边垂直布局
        JLabel jLabel0=new JLabel("内存的使用情况列表:(最坏适配算法)");
//        jLabel0.setFont(defaultFont);
        table = new JTable(model);                     //把模型对象作为参数构造表格对象，这样就可以用表格显示出数据
        DefaultTableCellRenderer   r   =   new DefaultTableCellRenderer();
        r.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class,   r);
        JScrollPane scroll = new JScrollPane(table);   //把表格控件放到滚动容器里，页面不够长显示可以滚动
        scroll.setPreferredSize(new Dimension(600, 300));
        vtemp1.add(jLabel0);
        vtemp1.add(Box.createVerticalStrut(5));                  //创建上下空间距离
        vtemp1.add(scroll);
        //右边垂直布局
        JLabel jLabel1=new JLabel("已用的内存空间:");
        jLabel2=new JLabel("0k");
        jLabel2.setFont(defaultFont);
//        jLabel2.setOpaque(true);
//        jLabel2.setBackground(Color.white);
        JLabel jLabel3=new JLabel("可用的内存空间:");
        jLabel4=new JLabel("1024k");
//        jLabel4.setPreferredSize(new Dimension(50,100));
        jLabel4.setFont(defaultFont);
//        jLabel4.setOpaque(true);
//        jLabel4.setBackground(Color.white);
        JLabel jLabel5=new JLabel("内存总的使用情况:");
        progressBar=new JProgressBar();
        progressBar.setStringPainted(true);
        vtemp2.add(jLabel1);
        vtemp2.add(Box.createVerticalStrut(30));                  //创建上下空间距离
        vtemp2.add(jLabel2);
        vtemp2.add(Box.createVerticalStrut(30));                  //创建上下空间距离
        vtemp2.add(jLabel3);
        vtemp2.add(Box.createVerticalStrut(30));                  //创建上下空间距离
        vtemp2.add(jLabel4);
        vtemp2.add(Box.createVerticalStrut(30));                  //创建上下空间距离
        vtemp2.add(jLabel5);
        vtemp2.add(Box.createVerticalStrut(30));                  //创建上下空间距离
        vtemp2.add(progressBar);
        //-------
        htemp1.add(vtemp1);
        htemp1.add(Box.createHorizontalStrut(40));//创建间隔
        htemp1.add(vtemp2);
        //第二列水平布局
        createButton=new JButton("创建进程");
        createButton.addActionListener(this);
        deleteButton=new JButton("撤销进程");
        deleteButton.addActionListener(this);
        exitButton=new JButton("退出程序");
        exitButton.addActionListener(this);
        htemp2.add(createButton);
        htemp2.add(Box.createHorizontalStrut(200));//创建间隔
        htemp2.add(deleteButton);
        htemp2.add(Box.createHorizontalStrut(200));//创建间隔
        htemp2.add(exitButton);
        //-------------------容器内容------------------------------
        vtemp.add(htemp1);
        vtemp.add(Box.createVerticalStrut(15));                  //创建上下空间距离
        vtemp.add(htemp2);
        showPanel.add(vtemp);
        baseBox.add(showPanel);
        this.add(baseBox, BorderLayout.NORTH);
    }
    private void createProcess(){
        int size=(int) (Math.random() * 200);
        Model freeModel=findBigest();
        cut(size,freeModel);
//        progressBar.setValue(currentProgress);
    }
    private void deleteProcess(int name){
        Model model=findModelByName(name);
        if(model==null){
            JOptionPane.showMessageDialog(null, "找不到"+Integer.toString(name)+"号进程", "警告", JOptionPane.WARNING_MESSAGE);
        }else {
            model.releaseModel();
            updateData();
        }
    }
    private Model findModelByName(int name){
        for(int i=0;i<blockList.size();i++){
            if(blockList.get(i).name==name){
                return blockList.get(i);
            }
        }
        return null;
    }
    private Model findBigest(){
        int max=-1;
        int index=-1;
        for(int i=0;i<blockList.size();i++){
            if(blockList.get(i).flag==false&&blockList.get(i).size>max){
                max=blockList.get(i).size;
                index=i;
            }
        }
        if(max!=-1){
//            System.out.println("不空");
            return blockList.get(index);
        }else {
//            System.out.println("空");
            return null;
        }
    }
    private void cut(int size,Model model){
        if(size>model.size){
            JOptionPane.showMessageDialog(null, "随机创建的进程大小为"+size+"k\n找不到可分配内存的可用分区", "警告", JOptionPane.WARNING_MESSAGE);
        }else{
            blockList.remove(model);
            int begin=model.begin;
            int formerSize=model.size;
            model.useModel(size);
            blockList.add(model);
            int newBegin=begin+size+1;
            int newSize=formerSize-size;
            createFreeBlock(newBegin,newSize);
            updateData();
        }

    }
    private void createFreeBlock(int begin,int size){
        Model model=new Model(beginNumber,size,begin,false);
        beginNumber++;
        blockList.add(model);
        updateData();
    }
    private void initBlockList(){
        blockList.add(block);
        beginNumber++;
    }
    private void updateData(){
        data.clear();
        int user=0;
        int notUse=0;
        int all=0;
        for(int i=0;i<blockList.size();i++){
            Vector<Object> v = new Vector<Object>();
            Model model=blockList.get(i);
            v.add(new String(Integer.toString(model.name)));
            v.add(new String(Integer.toString(model.size)));
            v.add(new String(Integer.toString(model.begin)));
//            v.add(new String(Integer.toString(model.size)));
            if(model.flag==true){
                v.add(new String(Integer.toString(model.name)+"号进程占用"));
                user+=model.size;
            }else {
                v.add(new String("可用"));
                notUse+=model.size;
            }
            all+=model.size;
            data.add(v);
        }
        Collections.sort(data, new Comparator() {
            public int compare(Object left, Object right){
                Vector<String> a=(Vector<String>)left;
                Vector<String> b=(Vector<String>)right;
//                System.out.println(a.get(0)+"    "+b.get(0));
                int a_temp=Integer.parseInt(a.get(0));
                int b_temp=Integer.parseInt(b.get(0));
                return a_temp-b_temp;
            }
        });
        jLabel2.setText(Integer.toString(user)+"k");
        jLabel4.setText(Integer.toString(notUse)+"k");
        float user_temp=user;
        float notUse_temp=notUse;
        float currentProgress=(user_temp/(user_temp+notUse_temp))*100;
        System.out.println(Float.toString(currentProgress));
        progressBar.setValue((int)currentProgress);
        model.addTableItem();                          //自适应行数
        table.repaint();
    }
    private void updaterogressBar(){
        int user=0;
        int notUse=0;
        int all=0;

    }
    class MyTableModel extends AbstractTableModel  //模型类
    {

        Vector<String> title = new Vector<String>();// 列名


        /**
         * 构造方法，初始化二维动态数组data对应的数据
         */
        public MyTableModel()                       //构造方法
        {
            title.add("分区号");

            title.add("大小（k）");

            title.add("起址（k）");

            title.add("状态");



        }

        // 以下为继承自AbstractTableModle的方法，可以自定义
        /**
         * 得到列名
         */
        @Override
        public String getColumnName(int column)
        {
            return title.elementAt(column);
        }

        /**
         * 重写方法，得到表格列数
         */
        @Override
        public int getColumnCount()
        {
            return title.size();
        }

        /**
         * 得到表格行数
         */
        @Override
        public int getRowCount()
        {
            return data.size();
        }

        /**
         * 得到数据所对应对象
         */
        @Override
        public Object getValueAt(int rowIndex, int columnIndex)
        {
            //return data[rowIndex][columnIndex];
            return data.elementAt(rowIndex).elementAt(columnIndex);
        }

        /**
         * 得到指定列的数据类型
         */
        @Override
        public Class<?> getColumnClass(int columnIndex)
        {
            return data.elementAt(0).elementAt(columnIndex).getClass();
        }

        /**
         * 指定设置数据单元是否可编辑.
         */
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex)
        {
            return false;
        }

        /*
         * 适配数据行数
         */
        public void addTableItem() {
            fireTableDataChanged();
        }

    }
}
