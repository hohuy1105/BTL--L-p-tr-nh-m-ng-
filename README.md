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

## 🚀 3. Hình ảnh chức năng 
<div align="center">

  <img width="500" alt="Giao diện Web Dashboard Trống" src="https://github.com/user-attachments/assets/0b2a8d89-d8d4-4ba2-a996-f1c9e7b1a893" />
  <p><b>Hình 1:</b> Trạng thái bãi đỗ xe trống rỗng 100% ban đầu khi vừa khởi động (Màu xanh chủ đạo).</p>

  <img width="500" alt="Luồng AI OCR bật Webcam chụp ảnh thật" src="https://github.com/user-attachments/assets/a05cb1cb-6233-4fdc-9946-4092c6ca0e83" />
  <p><b>Hình 2:</b> Thiết bị ESP32 chạm cảm biến vật lý -> Gọi Webcam tự động kích hoạt chụp hình quét AI OCR.</p>

  <img width="500" alt="Đồng bộ ô đỗ đổi màu Đỏ và tìm xe qua Chatbot" src="https://github.com/user-attachments/assets/3720122c-3703-4e1b-ac20-ef0f63b04223" />
  <p><b>Hình 3:</b> Ô đỗ số hóa tự động đổi màu Đỏ đồng bộ Real-time và giao tiếp tìm xe thông qua Trợ lý số Chatbot AI.</p>

</div>

## 📝 4. Hướng dẫn cài đặt và sử dụng

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
