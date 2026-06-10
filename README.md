<h2 align="center">
    <a href="https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin">
    🎓 Faculty of Information Technology (DaiNam University)
    </a>
</h2>
<h2 align="center">
   HỆ THỐNG QUẢN LÝ BÃI ĐỖ XE THÔNG MINH TRUNG TÂM (IOC CENTER)
</h2>
<div align="center">
    <p align="center">
        <img src="docs/aiotlab_logo.png" alt="AIoTLab Logo" width="170"/>
        <img src="docs/fitdnu_logo.png" alt="FIT DNU Logo" width="180"/>
        <img src="docs/dnu_logo.png" alt="DaiNam University Logo" width="200"/>
    </p>

[![AIoTLab](https://img.shields.io/badge/AIoTLab-green?style=for-the-badge)](https://www.facebook.com/DNUAIoTLab)
[![Faculty of Information Technology](https://img.shields.io/badge/Faculty%20of%20Information%20Technology-blue?style=for-the-badge)](https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin)
[![DaiNam University](https://img.shields.io/badge/DaiNam%20University-orange?style=for-the-badge)](https://dainam.edu.vn)

</div>

## 📖 1. Giới thiệu
Trong kỷ nguyên phát triển đô thị thông minh (Smart City), việc tối ưu hóa hạ tầng giao thông tĩnh bằng Trí tuệ nhân tạo (AI) và Internet vạn vật (IoT) là xu hướng tất yếu. Dự án xây dựng **"Hệ thống quản lý bãi đỗ xe thông minh ứng dụng AI và IoT"** (Smart Parking AI Center) ra đời nhằm thay thế các mô hình bãi xe truyền thống sử dụng vé giấy hoặc quẹt thẻ thủ công vốn dễ gây ùn tắc tại cổng tòa nhà vào giờ cao điểm.

Hệ thống hoạt động trên mô hình kiến trúc tích hợp toàn diện: Số hóa các ô đỗ vật lý thông qua mạch vi điều khiển kết nối không dây Wi-Fi ESP32, tự động chụp ảnh và giải mã biển số phương tiện bằng mô hình học sâu AI OCR tại máy chủ trung tâm, đồng bộ dữ liệu thời gian thực (Real-time) lên giao diện Web điều hành thông minh và hỗ trợ công dân tra cứu thông tin thông qua Trợ lý ảo Chatbot AI.

🔹 **Kiến trúc luồng xử lý dữ liệu hệ thống**

Một chu trình vận hành tự động bao gồm 4 bước chính:
1. **Phát hiện xe vật lý (IoT State):** Xe tiến vào vị trí đè lên cảm biến khoảng cách nối mạch ESP32, kích hoạt bộ lọc nhiễu tín hiệu `Debounce Delay (800ms)`.
2. **Truyền tải vô tuyến (HTTP API JSON):** Mạch ESP32 tự đóng gói dữ liệu trạng thái thành chuỗi tin định dạng JSON, bắn qua sóng Wi-Fi gọi trực tiếp tới API máy chủ Python Backend.
3. **Thị giác máy tính AI OCR:** Máy chủ FastAPI túc trực lệnh nhận diện, tự động kích hoạt phần cứng Webcam laptop chụp ảnh xe thật và đẩy vào mô hình học sâu mạng nơ-ron **EasyOCR** để giải mã chính xác chuỗi biển số Việt Nam.
4. **Đồng bộ số hóa đô thị (Polling UI):** Trạng thái ô đỗ trên bản đồ Web tự động nhảy sang **màu Đỏ** (Báo chiếm dụng) kèm biển số xe vừa quét mà không cần tải lại trang. Đồng thời, mạch ESP32 phát xung PWM điều khiển động cơ Servo quay $90^{\circ}$ mở thanh chắn Barrier cổng bãi xe.

## 🔧 2. Công nghệ sử dụng

* **Python & FastAPI:** Ngôn ngữ và Framework chính xây dựng lõi Máy chủ trung tâm hiệu năng cao, túc trực và mở dải host mạng ứng biến thích ứng `0.0.0.0:8000`.
* **Mô hình AI EasyOCR:** Tích hợp mạng cấu trúc học sâu (Deep Learning) CRAFT và CRNN để nhận diện ký tự quang học, xử lý ảnh trích xuất biển số xe tự động.
* **OpenCV:** Thư viện thị giác máy tính dùng để điều khiển và xử lý luồng ghi nhận hình ảnh Webcam laptop trực tiếp.
* **C++ & Arduino IDE:** Ngôn ngữ cấu trúc hệ thống và môi trường lập trình, biên dịch nhúng nạp firmware cho vi xử lý đầu cuối.
* **SoC Wi-Fi ESP32:** Chip vi điều khiển tích hợp card mạng vô tuyến không dây phụ trách đo đạc thực địa và điều khiển cơ cấu chấp hành.
* **Actuators (Servo & Cảm biến):** Động cơ Servo điều khiển thanh chắn Barrier giao thông vật lý và cảm biến hồng ngoại phát hiện khoảng cách xe.
* **HTML5 / CSS3 / JavaScript:** Xây dựng giao diện **Eco Smart City Dashboard** theo phong cách đồ họa sang trọng *Glassmorphism* (làm mờ nền kính công nghệ cao).


</div>

## 📝 3. Hướng dẫn cài đặt và sử dụng

Chương trình được phân tách biệt lập rõ ràng thành 3 phân hệ chức năng: `hardware` (ESP32), `backend` (Python FastAPI AI) và `frontend` (Web Dashboard). Dưới đây là hướng dẫn chi tiết các bước để cài đặt cấu hình và vận hành hệ thống.

## Hướng dẫn cài đặt

### 1. Cấu hình lõi phần cứng trên phần mềm Arduino IDE
* **Yêu cầu phần cứng:** 0.1 mạch SoC Wi-Fi ESP32, 0.1 cảm biến khoảng cách phản xạ hồng ngoại, 0.1 động cơ Servo SG90/MG996, dây cắm giắc nối cáp USB Mac dữ liệu.
* **Thêm đường dẫn gói mạch ESP32:**
  - Trên máy Mac, Huy mở Arduino IDE lên -> Nhấp chọn mục **Arduino IDE** trên thanh menu cao nhất -> Chọn **Settings...** (Preferences).
  - Tìm đến ô **`Additional Boards Manager URLs`** và dán chính xác đường dẫn mạng của hãng *Espressif Systems* sau:
    ```text
    [https://raw.githubusercontent.com/espressif/arduino-esp32/gh-pages/package_esp32_index.json](https://raw.githubusercontent.com/espressif/arduino-esp32/gh-pages/package_esp32_index.json)
    ```
  - Nhấn nút **OK** để lưu lại cấu hình.
* **Tải Driver Core ESP32:**
  - Nhìn sang thanh công cụ dọc ngoài cùng cạnh bên trái, nhấp chọn vào biểu tượng **Mảnh bo mạch có 4 chân chip** (mục *Boards Manager*).
  - Tại ô tìm kiếm trên cùng, gõ chữ: `esp32`. Tìm thấy gói mạch **`esp32` by `Espressif Systems`**, Huy nhấn nút **`Install`** màu xanh. Đợi khoảng 1 phút để máy tải về thành công cho đến khi chuyển sang màu xám báo chữ `INSTALLED`.
* **Nạp Code nhúng:**
  - Cắm cáp USB nối mạch ESP32 với máy Mac.
  - Trên thanh công cụ trên cùng, chọn ô trắng -> Click mục **Select other board and port...**
  - Tại ô tìm kiếm bên trái gõ tìm kiếm và chọn dòng: **`ESP32 Dev Module`**. Nhìn sang mục **Ports** bên phải tích chọn cổng nhận dạng cáp kết nối dạng `/dev/cu.usbserial-...`. Nhấn **OK**.
  - Huy mở file code nằm trong thư mục `hardware/plant-monitoring-system.ino` lên, sửa lại tên và mật khẩu mạng Wi-Fi lớp học, sau đó bấm nút mũi tên **Upload (➡️)** để nạp code xuống chip. *(Mẹo: Khi màn hình dưới chạy chữ báo `Connecting...___...`, Huy bấm giữ nút BOOT trên mạch 2 giây để mở cổng nạp).*

### 2. Cài đặt Máy chủ Trí tuệ nhân tạo Python Backend
* Huy mở khung Terminal nằm ngay trên ứng dụng phần mềm biên dịch VS Code và di chuyển thư mục, gõ lệnh cài đặt gói môi trường thư viện:
  ```bash
  cd backend
  pip install -r requirements.txt
Hệ thống sẽ tự động cấu hình và tải toàn bộ các gói phần mềm tối ưu: fastapi, uvicorn, easyocr, opencv-python.
3. Đồng bộ hóa liên kết Giao diện Web Frontend
Mở thư mục frontend/, đảm bảo tệp script logic liên kết điều khiển camera và chatbot dữ liệu mạng app.js đã nằm ngang hàng đồng bộ với file cấu trúc giao diện index.html.
Kiểm tra dòng số 1 của file app.js đã trỏ đúng cổng local của máy tính: const SERVER_URL = "http://localhost:8000";.
Hướng dẫn sử dụng
Hệ thống vận hành đồng bộ nhịp nhàng thông qua chuỗi 4 bước thao tác theo quy trình thực tế dưới đây:
Bước 1: Khởi động Máy chủ Trung tâm điều hành Python
Tại Terminal của thư mục dự án backend/, Huy gõ câu lệnh khởi động server chạy dải host mạng đa năng:
Bash
python3 main.py
Bạn hãy giữ nguyên màn hình cửa sổ Terminal này chạy ngầm không tắt. Khi hệ thống báo dòng trạng thái chữ xanh: INFO: Uvicorn running on http://0.0.0.0:8000 nghĩa là máy chủ AI đã sẵn sàng kết nối.
Bước 2: Bật màn hình Trung tâm điều hành Web Dashboard IOC
Huy nhấp chuột phải trực tiếp vào file frontend/index.html bên cột Explorer chọn Open with Live Server. Trình duyệt Chrome sẽ tự động bật mở giao diện đồ thị bãi đỗ xe thông minh.
Mẹo mở khóa Camera bảo mật trên Chrome: Trên thanh địa chỉ của trình duyệt Chrome hiện ra, Huy click chuột xóa số tĩnh 127.0.0.1 đi và sửa lại viết chữ thành chữ localhost (Đường dẫn dạng: http://localhost:5500/frontend/index.html) rồi gõ Enter. Trình duyệt hiện hộp thoại xin quyền máy ảnh, Huy bấm chọn nút Cho phép (Allow).
Thành quả rực rỡ: Đèn camera Webcam trên máy Mac tự động sáng và hình ảnh luồng video trực tiếp của bạn hiển thị căng nét ngay trên giao diện web, dòng chữ báo lỗi đỏ biến mất hoàn toàn!
Bước 3: Cấp nguồn thiết bị phần cứng thực địa
Huy cắm cáp cấp điện nguồn cho bo mạch ESP32 (Thông qua củ sạc hoặc sạc dự phòng). Mạch tự động nháy đèn kết nối vô tuyến vào mạng Wi-Fi lớp học.
Lúc này quan sát trên giao diện Web, cả 5 ô đỗ xe từ A-01 đến A-05 đều hiển thị đồng loạt màu Xanh dương (Trạng thái TRỐNG).
Bước 4: Demo kịch bản quy trình chạy thực tế Đồ án trước hội đồng
Xe tiến vào: Huy dùng một chiếc mô hình ô tô hoặc bàn tay che mắt cảm biến khoảng cách nối chân 32 của mạch ESP32. Mạch lập tức bắn gói tin mạng không dây HTTP truyền JSON lên máy Mac.
AI Kích hoạt: Máy chủ Python tự gọi camera chụp hình thật một ảnh, nạp vào lõi AI học sâu giải mã biển số xe (Ví dụ: 29A-888.88). Thanh chắn Servo cổng chân 22 tự động gạt góc 90 
∘
  mở Barrier. Đồng thời, ô đỗ A-01 trên Web Chrome tự động đổi màu Đỏ rực rỡ hiện biển xe mà không cần tải lại trang.
Hỏi đáp Trợ lý ảo Chatbot: Huy sang phân hệ ô cửa sổ bên phải gõ câu hỏi: "xe 29A88888 đỗ ở đâu?" và nhấn gửi. Trợ lý số thông minh tự động so khớp cơ sở dữ liệu thời gian thực và chat phản hồi định vị chuẩn xác: "📍 Định vị thành công! Xe của bạn đang đỗ an toàn tại vị trí ô số [A-01]". Gõ tiếp "giá vé bao nhiêu" -> AI phản hồi ngay lập tức bảng giá cước đô thị xanh.
👨‍💻 Thông tin nguồn Tác giả Đề tài
Học viện đào tạo: Trường Đại học Đại Nam (DaiNam University) - Khoa Công nghệ Thông tin.
Nghiên cứu sinh thực hiện: Hồ Quang Huy
Mã số sinh viên (MSSV): 1671020137
Phòng nghiên cứu ứng dụng: AIoTLab Đô thị thông minh.
Hòm thư liên hệ chính thức: hoquanghuyhb1@gmail.com
