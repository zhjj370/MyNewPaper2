package org.zhjj370.ga.element;

public class ElePosition {
    private int idPosition; //与匹配值表格中横坐标对应
    private int idtask;
    private double dNI;//交货期临近指数
    private double wT;//对应任务已经等待时间
    private int level;//工人等级
    private int matchWorker=-1;//
    private EleWorker eleWorker;//

    public ElePosition(int idPosition, int idtask, double dNI, double wT, int level){
        this.idPosition = idPosition;
        this.idtask = idtask;
        this.dNI = dNI;
        this.wT = wT;
        this.level = level;
    }

    public int getIdPosition() {
        return idPosition;
    }

    public int getIdtask() {
        return idtask;
    }

    public double getdNI() {
        return dNI;
    }

    public double getwT() {
        return wT;
    }

    public int getLevel() {
        return level;
    }

    public int getMatchWorker() {
        return matchWorker;
    }

    public void setMatchWorker(int matchWorker) {
        this.matchWorker = matchWorker;
    }

    public EleWorker getEleWorker() {
        return eleWorker;
    }

    public void setEleWorker(EleWorker eleWorker) {
        this.eleWorker = eleWorker;
    }
}
