package unt.cse.nsl.smartauscultation;

/**
 * Created by Anurag Chitnis on 2/6/2017.
 */

public enum AuscultationArea {
    AORTIC(1),
    PULMONARY(2),
    LLSB(3),
    MITRAL(4),
    NONE(0), // when no area is selected

    ANT_TRACHEA(1),
    ANT_UPPER_RIGHT_LUNG_FIELD(2),
    ANT_UPPER_LEFT_LUNG_FIELD(3),
    ANT_MIDDLE_RIGHT_LUNG_FIELD(4),
    ANT_MIDDLE_LEFT_LUNG_FIELD(5),
    ANT_LOWER_RIGHT_LUNG_FIELD(6),
    ANT_LOWER_LEFT_LUNG_FIELD(7),

    POS_UPPER_LEFT_LUNG_FIELD(0),
    POS_UPPER_RIGHT_LUNG_FIELD(1),
    POS_MIDDLE_RIGHT_LUNG_FIELD(2),
    POS_MIDDLE_LEFT_LUNG_FIELD(3),
    POS_LOWER_LEFT_LUNG_FIELD(4),
    POS_LOWER_RIGHT_LUNG_FIELD(5),
    POS_RIGHT_COSTOPHRENIC_ANGLE(6),
    POS_LEFT_COSTOPHRENIC_ANGLE(7);

    private int areaCode;

    AuscultationArea(int areaCode) {
        this.areaCode = areaCode;
    }

    public int getAreaCode() {
        return areaCode;
    }

    public static AuscultationArea getHeartAuscultationArea(int areaCode) {
        switch (areaCode) {
            case 1:
                return AORTIC;
            case 2:
                return PULMONARY;
            case 3:
                return LLSB;
            case 4:
                return MITRAL;
            default:
                return NONE;
        }
    }

}
