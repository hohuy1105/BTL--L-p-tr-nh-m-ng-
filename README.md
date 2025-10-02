<h2 align="center">
    <a href="https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin">
    🎓 Faculty of Information Technology (DaiNam University)
    </a>
</h2>
<h2 align="center">
   TRUYỀN FILE QUA UDP
</h2>
<div align="center">
    <p align="center">
        <img src="docs/aiotlab_logo.png" alt="AIoTLab Logo" width="170"/>
        <img src="docs/fitdnu_logo.png" alt="AIoTLab Logo" width="180"/>
        <img src="docs/dnu_logo.png" alt="DaiNam University Logo" width="200"/>
    </p>

[![AIoTLab](https://img.shields.io/badge/AIoTLab-green?style=for-the-badge)](https://www.facebook.com/DNUAIoTLab)
[![Faculty of Information Technology](https://img.shields.io/badge/Faculty%20of%20Information%20Technology-blue?style=for-the-badge)](https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin)
[![DaiNam University](https://img.shields.io/badge/DaiNam%20University-orange?style=for-the-badge)](https://dainam.edu.vn)

</div>

## 📖 1. Giới thiệu
UDP là một trong những giao thức truyền tải chính của bộ giao thức Internet (TCP/IP). Khác với TCP, vốn cần thiết lập kết nối và đảm bảo tính toàn vẹn dữ liệu, UDP hoạt động theo mô hình không kết nối (connectionless). Điều này có nghĩa là khi một ứng dụng gửi dữ liệu, các gói tin (datagram) sẽ được truyền trực tiếp đến địa chỉ đích mà không cần bất kỳ thủ tục bắt tay (handshake) nào.

Đặc điểm quan trọng:

Đơn giản và nhẹ: UDP không duy trì trạng thái phiên làm việc, giúp tiết kiệm tài nguyên hệ thống.
Hiệu năng cao: Tốc độ truyền nhanh do không có bước kiểm tra, xác nhận hay sắp xếp lại gói tin.
Không tin cậy tuyệt đối: Gói tin có thể bị mất, trùng lặp hoặc đến sai thứ tự mà không có cơ chế tự động khắc phục.
Khả năng broadcast/multicast: UDP hỗ trợ gửi dữ liệu đến nhiều thiết bị cùng lúc, phù hợp cho các ứng dụng truyền thông nhóm.
Ứng dụng thực tế:
UDP thường được sử dụng trong các hệ thống cần tốc độ và độ trễ thấp hơn là độ tin cậy tuyệt đối, ví dụ:
Trò chơi trực tuyến (online gaming): truyền thông tin trạng thái nhân vật theo thời gian thực.
Ứng dụng gọi thoại/video (VoIP, video call): truyền âm thanh, hình ảnh với độ trễ thấp.
Streaming (truyền phát video, audio): ưu tiên tốc độ để người dùng xem liên tục.
DNS (Domain Name System): truy vấn tên miền cần phản hồi nhanh, không nhất thiết phải có kết nối lâu dài.

🔹 Cấu trúc gói tin UDP

Một gói tin UDP bao gồm 4 phần chính:

Source Port (16 bit): Cổng nguồn.

Destination Port (16 bit): Cổng đích.

Length (16 bit): Độ dài toàn bộ gói UDP.

Checksum (16 bit): Kiểm tra lỗi cơ bản.

🔹 Ứng dụng của UDP

UDP thường được dùng trong các ứng dụng cần tốc độ hơn độ tin cậy tuyệt đối:

Truyền phát video/audio trực tuyến (YouTube, Zoom, VoIP).

Game online (yêu cầu phản hồi nhanh, chấp nhận mất gói).

DNS (Domain Name System).

Streaming đa phương tiện, IPTV.

## 🔧 2. Công nghệ sử dụng

Java: ngôn ngữ lập trình chính.

UDP (User Datagram Protocol): giao thức truyền nhanh, không kết nối.

Socket lập trình mạng: DatagramSocket, DatagramPacket.

Java IO: đọc file (FileInputStream), ghi file (FileOutputStream).

Header Sequence Number: số thứ tự gói tin để ghép file đúng.

## 🚀 3. Hình ảnh chức năng 
<div align="center">

  <img width="363" height="209" alt="Ảnh màn hình 2025-10-02 lúc 08 10 01" src="https://github.com/user-attachments/assets/0b2a8d89-d8d4-4ba2-a996-f1c9e7b1a893" />
  <p><b>Hình 1:</b> Tab "đăng nhập" .</p>
<img width="361" height="211" alt="Ảnh màn hình 2025-10-02 lúc 08 08 16" src="https://github.com/user-attachments/assets/a05cb1cb-6233-4fdc-9946-4092c6ca0e83" />
 
  <p><b>Hình 2:</b> Tab "đăng kí" .</p>

<img width="965" height="718" alt="Ảnh màn hình 2025-10-02 lúc 08 07 41" src="https://github.com/user-attachments/assets/3720122c-3703-4e1b-ac20-ef0f63b04223" />

  <p><b>Hình 3:</b> Tab "giao diện chính".</p>


</div>

## 📝 4. Hướng dẫn cài đặt và sử dụng

- Dựa trên mã nguồn được cung cấp, ứng dụng này là một chương trình Java sử dụng giao thức UDP để truyền tệp giữa các peer, với giao diện người dùng (GUI) được xây dựng bằng Swing và FlatLaf. Dưới đây là hướng dẫn chi tiết để cài đặt và sử dụng ứng dụng này.

## Hướng dẫn cài đặt
- Yêu cầu hệ thống

Java Development Kit (JDK): Cần cài đặt JDK 8 hoặc cao hơn (khuyến nghị JDK 17 hoặc mới hơn để đảm bảo tương thích).
Hệ điều hành: Windows, macOS, hoặc Linux.
Thư viện FlatLaf: Ứng dụng sử dụng FlatLaf để cải thiện giao diện. Bạn cần tải và thêm thư viện này vào dự án.
Môi trường phát triển (tùy chọn): IntelliJ IDEA, Eclipse, hoặc bất kỳ IDE nào hỗ trợ Java.
Mạng: Các thiết bị chạy ứng dụng cần nằm trong cùng mạng nội bộ (LAN) hoặc có thể truy cập qua địa chỉ IP công cộng.

## Các bước cài đặt

- Cài đặt JDK:

Tải JDK từ trang chính thức của Oracle hoặc sử dụng OpenJDK.
Cài đặt và cấu hình biến môi trường JAVA_HOME và thêm bin vào PATH.
Kiểm tra cài đặt bằng lệnh:
bashjava -version



- Tải thư viện FlatLaf:

Tải FlatLaf từ Maven Repository hoặc trang GitHub của FlatLaf.
Thêm tệp JAR (flatlaf-<version>.jar) vào dự án:

Nếu dùng IDE, thêm JAR vào thư mục lib và cấu hình trong build path.
Nếu dùng Maven, thêm dependency vào pom.xml:
xml<dependency>
    <groupId>com.formdev</groupId>
    <artifactId>flatlaf</artifactId>
    <version>3.2.5</version> <!-- Kiểm tra phiên bản mới nhất -->
</dependency>





- Tạo dự án Java:

Tạo một dự án Java mới trong IDE hoặc sử dụng một thư mục dự án thủ công.
Sao chép các tệp mã nguồn được cung cấp (LoginFrame.java, UDPPeer.java, PacketType.java, UserManagementDialog.java, HistoryService.java, AuthService.java, HistoryEntry.java, CryptoUtils.java, User.java) vào thư mục src của dự án.
Tạo thư mục resources/icons và thêm các tệp icon (login.png, register.png, play.png, stop.png, send.png, folder.png, users.png, connected.png, disconnected.png, logout.png) vào đó, vì các tệp này được tham chiếu trong mã nguồn.


- Cấu hình dự án:

Đảm bảo tất cả các lớp Java được biên dịch mà không có lỗi.
Nếu sử dụng IDE, cấu hình để chạy lớp LoginFrame (chứa phương thức main).


- Biên dịch và chạy chương trình:

Nếu dùng IDE, nhấn nút "Run" để biên dịch và chạy LoginFrame.
Nếu dùng dòng lệnh:
bashjavac -cp .;lib/flatlaf-<version>.jar src/*.java
java -cp .;lib/flatlaf-<version>.jar LoginFrame
(Thay ; bằng : trên macOS/Linux, và thay lib/flatlaf-<version>.jar bằng đường dẫn thực tế đến tệp FlatLaf JAR.)


- Kiểm tra tệp dữ liệu:

Ứng dụng lưu thông tin người dùng trong users.dat và lịch sử trong history.dat. Đảm bảo thư mục làm việc có quyền ghi để tạo các tệp này.
Tài khoản admin mặc định (admin/admin) sẽ được tạo tự động nếu tệp users.dat không tồn tại.




## Hướng dẫn sử dụng

Ứng dụng cung cấp giao diện để đăng nhập, đăng ký, và truyền tệp giữa các peer qua giao thức UDP. Dưới đây là các bước sử dụng:

## 1. Đăng nhập hoặc đăng ký

- Mở ứng dụng:

Chạy chương trình, cửa sổ đăng nhập (LoginFrame) sẽ hiện ra.


- Đăng nhập:

Nhập tên người dùng và mật khẩu vào các trường tương ứng.
Nhấn nút Đăng nhập (có icon khóa).
Nếu thông tin đúng, bạn sẽ vào giao diện chính (UDPPeer). Nếu sai, một thông báo lỗi sẽ hiện ra.
Tài khoản mặc định: admin/admin (vai trò ADMIN).


- Đăng ký:

Nhấn nút Đăng ký (có icon người dùng).
Nhập tên người dùng và mật khẩu mới qua các hộp thoại.
Nếu đăng ký thành công, bạn sẽ được yêu cầu đăng nhập lại.



## 2. Giao diện chính (UDPPeer)

Giao diện chính bao gồm:

Bảng điều khiển trên cùng:

Trường nhập Local Port (cổng cục bộ để lắng nghe).
Trường nhập Remote IP và Remote Port (địa chỉ IP và cổng của peer nhận).
Nút Bắt đầu Lắng nghe (Play) hoặc Dừng (Stop).
Nút Chọn tệp (Browse) để chọn tệp/thư mục gửi.
Nút Gửi (Send) để bắt đầu truyền tệp.
Nút Đổi thư mục lưu để chọn nơi lưu tệp nhận được.
Nhãn trạng thái (Status) và nhãn đường dẫn lưu tệp.


Bảng lịch sử (History Table): Hiển thị các giao dịch truyền/nhận tệp (hành động, tên tệp, kích thước, peer, thời gian).
Khu vực nhật ký (Log Area): Hiển thị thông tin chi tiết về hoạt động (ví dụ: gửi/nhận tệp, lỗi).
Thanh tiến độ: Hiển thị tiến độ truyền tệp hiện tại và tổng thể, cùng với tốc độ và thời gian còn lại (ETR).

## 3. Truyền tệp

Chuẩn bị:

Peer nhận: Mở ứng dụng trên thiết bị nhận, nhập một cổng cục bộ (ví dụ: 12345) vào Local Port, và nhấn Bắt đầu Lắng nghe. Nhãn trạng thái sẽ chuyển thành "Đang lắng nghe..." với icon kết nối.
Peer gửi: Mở ứng dụng trên thiết bị gửi, nhập địa chỉ IP (ví dụ: 192.168.1.100) và cổng (ví dụ: 12345) của peer nhận vào Remote IP và Remote Port.


Chọn tệp:

Trên peer gửi, nhấn Chọn tệp để mở hộp thoại chọn tệp/thư mục, hoặc kéo thả tệp/thư mục vào giao diện.
Danh sách tệp sẽ được hiển thị trong nhãn tiến độ (progressLabel).


Gửi tệp:

Nhấn Gửi để bắt đầu truyền tệp. Thanh tiến độ, tốc độ, và thời gian còn lại sẽ cập nhật trong quá trình truyền.
Nhật ký (logArea) sẽ ghi lại các sự kiện như "Đã chọn X tệp", "Bắt đầu gửi tệp Y".


Nhận tệp:

Peer nhận tự động lưu tệp vào thư mục được chỉ định (saveDirectory, hiển thị trong savePathLabel).
Lịch sử truyền/nhận được ghi vào bảng lịch sử (historyTable).



## 4. Quản lý người dùng (chỉ dành cho ADMIN)

Nếu đăng nhập với tài khoản admin, nhấn nút có icon người dùng trong giao diện UDPPeer để mở UserManagementDialog.
Thêm người dùng:

Nhấn Thêm, nhập tên người dùng và mật khẩu mới.
Người dùng mới sẽ được thêm với vai trò USER.


Xóa người dùng:

Chọn một người dùng trong danh sách và nhấn Xóa.
Lưu ý: Không thể xóa tài khoản admin.



## 5. Xem lịch sử

Bảng lịch sử (historyTable) hiển thị các giao dịch truyền/nhận tệp của người dùng hiện tại.
Nhấp chuột phải vào một dòng để mở menu ngữ cảnh với tùy chọn:

Mở File: Mở tệp nếu nó tồn tại.
Hiển thị trong Thư mục: Mở thư mục chứa tệp.



## 6. Đăng xuất

Nhấn nút có icon đăng xuất trong giao diện UDPPeer để quay lại màn hình đăng nhập.




## Nguồn

DaiNam University.

Hồ Quang Huy - 1671020137

Gmail: hoquanghuyhb1@gmail.com



---
