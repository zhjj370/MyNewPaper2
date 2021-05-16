package org.zhjj370.ga.element;

public class EleTask {
    private int idtask;
    private double dNI;//交货期临近指数
    private double dNI_Normalized = 0.0;
    private double wT;//对应任务已经等待时间
    private double wT_Normalized = 0.0;
    private int aMatch=-1;//是否配对成功
    private double totalAbilityValue = 0.0;//能力值总数
    private int needT;//还需多长时间
    private double dNI_wT;

    public EleTask(int idtask,double dNI,double wT){
        this.idtask = idtask;
        this.dNI = dNI;
        this.wT = wT;
    }

    public void normalizedBefore(double max_dNI,double max_wT,double a,double b){
        if(max_dNI>0) dNI_Normalized = dNI/max_dNI;
        if(max_wT>0)  wT_Normalized = max_wT/(max_wT+wT);
        dNI_wT = a*dNI_Normalized + b*wT_Normalized;
    }

    public void reset(){
        this.aMatch = -1;
        this.needT = 0;
        this.totalAbilityValue = 0.0;
    }

    public double getdNI_wT() {
        return dNI_wT;
    }

    public double getdNI_Normalized() {
        return dNI_Normalized;
    }

    public double getwT_Normalized() {
        return wT_Normalized;
    }

    public void setwT_Normalized(double wT_Normalized) {
        this.wT_Normalized = wT_Normalized;
    }

    public double getwT() {
        return wT;
    }

    public double getdNI() {
        return dNI;
    }

    public int getIdtask() {
        return idtask;
    }

    public int getaMatch() {
        return aMatch;
    }

    public int getNeedT() {
        return needT;
    }


    public void setaMatch(int aMatch) {
        this.aMatch = aMatch;
    }

    public void congestW(double w){
        this.totalAbilityValue = this.totalAbilityValue+w;
    }

    public double getTotalAbilityValue() {
        return totalAbilityValue;
    }

    public void setNeedT(int needT) {
        this.needT = needT;
    }
}
