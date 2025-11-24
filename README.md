## Cách chạy game
Bước 1: Tải và giải nén thư mục game.
Bước 2: Nhấp đúp chuột vào file:
Game.exe
Game sẽ khởi động trực tiếp vào Main Menu, không yêu cầu cài đặt thêm.



## Cài đặt thư viện (Gson)
Vì project được kéo từ GitHub về IDE sẽ không tự nhận classpath.
Người dùng phải thêm file JAR thủ công:

* IntelliJ IDEA

1. Mở menu: File → Project Structure
2. Chọn Libraries
3. Nhấn +
4. Chọn file:
    2DJavaGame/libraries/gson-2.10.1.jar
5. Nhấn Apply → OK

* NetBeans

1. Chuột phải Project → Properties
2. Chọn Libraries
3. Nhấn Add JAR/Folder
4. Chọn file:
    libraries/gson-2.10.1.jar
5. OK


## Lỗi phổ biến & cách khắc phục
1.	Mở EXE nhưng không chạy
Kiểm tra Windows SmartScreen, chọn “Run Anyway”.
2.	Chạy nhưng mất ảnh/map/sound
Đảm bảo thư mục resources/ nằm cùng cấp với Game.exe.
3.	Load Game lỗi
Xoá file saves/savegame.json, game sẽ tạo file mới.


## Hướng dẫn chơi nhanh
-	W A S D – Di chuyển
-	J / – Tấn công 
-	E , F– Tương tác / nói chuyện
-	F5 để save game
-	ESC – Mở Pause Menu
-	PLAY – Bắt đầu chơi
-	OPTION – Tải lại file save
-	CREDITS – Hiển thị thành viên nhóm
Game hỗ trợ:
-	Combat có 3 phase tấn công
-	Knockback
-	AI đuổi/chạy/lảng vảng
-	Map chia theo Chunk tải động
-	UI có Menu, Pause, GameOver
-	Save/Load bằng JSON
-	Âm thanh nền + hiệu ứng sound
