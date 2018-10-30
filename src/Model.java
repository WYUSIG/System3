public class Model implements Comparable<Model>{
    public int name;
    public int begin;
    public int end;
    public int size;
    public boolean flag;
    public Model(int name,int size,int begin,boolean flag){
        this.name=name;
        this.begin=begin;
        this.end=begin+size;
        this.size=size;
        this.flag=flag;
    }
    public void useModel(int size){
        this.end=begin+size;
        this.size=size;
        this.flag=true;
    }
    public void releaseModel(){
        this.flag=false;
    }
    @Override
     public int compareTo(Model s) {
         //自定义比较方法，如果认为此实体本身大则返回1，否则返回-1
         if(this.name >= s.name){
             return 1;
         }
         return -1;
     }
}
