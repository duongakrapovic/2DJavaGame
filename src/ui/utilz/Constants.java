package ui.utilz;

public class Constants {

    // ===== UI constants =====
    public static class UI {
        public static class MenuButtons {
            public static final int MENU_BUTTON_SIZE_W = 140;
            public static final int MENU_BUTTON_SIZE_H = 56;
        }

        // ===== Pause Menu Layout =====
        public static class PauseButtons {
            public static final int SOUND_SIZE = 42;   // Kích thước nút loa
            public static final float MUSIC_Y = 0.33f; // Vị trí tương đối theo ảnh nền
            public static final float SFX_Y   = 0.48f;
            public static final float VOLUME_Y = 0.63f;

            public static final float BUTTON_OFFSET_X = 0.18f; // dịch nút sang phải so với tâm khung
            public static final float GROUP_Y = 0.80f;         // vị trí nhóm URM
            public static final int URM_GAP = 8;               // khoảng cách giữa các nút URM

            public static final float MUSIC_CENTER_Y_PCT = 0.405f;   // vị trí tương đối theo chiều cao
            public static final float MUSIC_WIDTH_PCT    = 0.66f;    // độ rộng tương đối của khung chữ MUSIC
            public static final int   MUSIC_SOUND_GAP    = 6;        // khoảng cách giữa chữ MUSIC và nút
            public static final int   MUSIC_SOUND_Y_OFFSET = -4;
        }

        // ===== URM Buttons =====
        public static class URMButtons {
            public static final int URM_SIZE = 70;
        }
    }

}
