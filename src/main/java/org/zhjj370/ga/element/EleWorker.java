package org.zhjj370.ga.element;

public class EleWorker {
    private int id;
    private int level;
    private int needTime;
    public EleWorker(int id, int level,int needTime){
        this.id = id;
        this.level = level;
        this.needTime = needTime;
    }

    public int getLevel() {
        return level;
    }

    public int getId() {
        return id;
    }

    public int getNeedTime() {
        return needTime;
    }
}
