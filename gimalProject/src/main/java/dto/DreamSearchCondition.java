package dto;

import java.util.List;

public class DreamSearchCondition {

    private String dong;                // 동 단위 위치 (예: "마곡동")
    private String keyword;             // 검색어
    private String categoryCode;        // 카테고리 코드
    private boolean excludeDone;        // 나눔완료 제외
    private boolean newOnly;            // 새상품만 보기
    private List<String> conditionCodes; // 상태 체크박스들(NEW / LIKE_NEW / USED)
    private String sort; 				 // LATEST / OLDEST
    
    public String getDong() {
        return dong;
    }

    public void setDong(String dong) {
        this.dong = dong;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public boolean isExcludeDone() {
        return excludeDone;
    }

    public void setExcludeDone(boolean excludeDone) {
        this.excludeDone = excludeDone;
    }

    public boolean isNewOnly() {
        return newOnly;
    }

    public void setNewOnly(boolean newOnly) {
        this.newOnly = newOnly;
    }

    public List<String> getConditionCodes() {
        return conditionCodes;
    }

    public void setConditionCodes(List<String> conditionCodes) {
        this.conditionCodes = conditionCodes;
    }
    
    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
