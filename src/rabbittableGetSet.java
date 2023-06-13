public class rabbittableGetSet {

    public String name; //유저 이름
    public int first_score; //첫번째 스테이지 점수
    public int first_clear; //첫번째 스테이지 클리어 여부
    //true(클리어) = 1 / false(클리어ㄴㄴ) = 0
    public int second_score; //두번째 스테이지 점수
    public int second_clear; //두번째 스테이지 클리어 여부

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFirst_score() {
        return first_score;
    }

    public void setFirst_score(int first_score) {
        this.first_score = first_score;
    }

    public int getFirst_clear() {
        return first_clear;
    }

    public void setFirst_clear(int first_clear) {
        this.first_clear = first_clear;
    }

    public int getSecond_score() {
        return second_score;
    }

    public void setSecond_score(int second_score) {
        this.second_score = second_score;
    }

    public int getSecond_clear() {
        return second_clear;
    }

    public void setSecond_clear(int second_clear) {
        this.second_clear = second_clear;
    }

}
