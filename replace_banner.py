import re

with open('src/main/resources/templates/web/index.html', 'r') as f:
    content = f.read()

# Define the new banner html
new_banner = """
        <!-- Main Section: Banner (Full Width) -->
        <div class="container-fluid p-0 mb-5 px-3 pt-3">
            <!-- Thư viện Devicon cho các icon công nghệ -->
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/devicon.min.css">
            
            <style>
                .tech-banner {
                    background-color: #0d1117;
                    background-image: 
                        linear-gradient(rgba(13, 17, 23, 0.85), rgba(21, 14, 38, 0.9)),
                        linear-gradient(90deg, rgba(0, 255, 255, 0.05) 1px, transparent 1px),
                        linear-gradient(rgba(255, 0, 255, 0.05) 1px, transparent 1px);
                    background-size: cover, 30px 30px, 30px 30px;
                    background-position: center;
                    border-radius: 20px;
                    padding: 40px;
                    color: #ffffff;
                    font-family: 'Inter', 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5), inset 0 0 20px rgba(0, 255, 255, 0.1);
                    position: relative;
                    overflow: hidden;
                    border: 1px solid rgba(255, 255, 255, 0.1);
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    flex-wrap: wrap;
                    gap: 30px;
                }
                .tech-banner::before {
                    content: ''; position: absolute; top: -50%; left: -10%;
                    width: 300px; height: 300px;
                    background: radial-gradient(circle, rgba(0, 255, 255, 0.2) 0%, transparent 70%);
                    border-radius: 50%; pointer-events: none;
                }
                .tech-banner::after {
                    content: ''; position: absolute; bottom: -50%; right: -10%;
                    width: 300px; height: 300px;
                    background: radial-gradient(circle, rgba(255, 0, 255, 0.2) 0%, transparent 70%);
                    border-radius: 50%; pointer-events: none;
                }
                .banner-content { flex: 1 1 60%; z-index: 1; }
                .banner-label {
                    display: inline-block; background-color: #ffd700; color: #000000;
                    font-weight: 700; font-size: 0.85rem; padding: 6px 12px;
                    border-radius: 6px; margin-bottom: 15px; text-transform: uppercase;
                    letter-spacing: 1px;
                }
                .banner-heading { font-size: 2.5rem; font-weight: 800; margin: 0 0 15px 0; line-height: 1.3; }
                .text-gradient {
                    background: linear-gradient(90deg, #00d2ff 0%, #ff00ff 100%);
                    -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text;
                }
                .banner-subheading { font-size: 1.1rem; color: #a0aec0; margin-bottom: 25px; line-height: 1.5; max-width: 90%; }
                .tech-icons { display: flex; gap: 20px; font-size: 2rem; align-items: center; }
                .tech-icons i { transition: transform 0.3s ease, filter 0.3s ease; }
                .tech-icons i:hover { transform: translateY(-5px); filter: drop-shadow(0 0 10px rgba(255, 255, 255, 0.5)); }
                .banner-action { display: flex; flex-direction: column; align-items: center; justify-content: center; z-index: 1; }
                .cta-button {
                    background: linear-gradient(90deg, #007bff, #6f42c1); color: #ffffff;
                    font-size: 1.1rem; font-weight: 700; padding: 15px 35px; border: none;
                    border-radius: 50px; cursor: pointer; text-decoration: none;
                    display: inline-flex; align-items: center; gap: 10px; transition: all 0.3s ease;
                    box-shadow: 0 4px 15px rgba(111, 66, 193, 0.4);
                }
                .cta-button:hover { transform: scale(1.05); box-shadow: 0 6px 20px rgba(111, 66, 193, 0.6); color: #fff; background: linear-gradient(90deg, #0056b3, #59359a); }
                .cta-subtext { font-size: 0.85rem; color: #cbd5e1; margin-top: 15px; opacity: 0.8; text-align: center; }
                @media (max-width: 768px) {
                    .tech-banner { flex-direction: column; text-align: center; padding: 30px 20px; }
                    .banner-content { flex: 1 1 100%; }
                    .banner-heading { font-size: 2rem; }
                    .banner-subheading { margin: 0 auto 20px auto; }
                    .tech-icons { justify-content: center; margin-bottom: 20px; }
                    .banner-action { width: 100%; }
                    .cta-button { width: 100%; justify-content: center; }
                }
            </style>
            
            <div class="tech-banner">
                <div class="banner-content">
                    <div class="banner-label">HOT SERVICE</div>
                    <h2 class="banner-heading text-white">
                        Tự động hóa & Tối ưu hóa <br />
                        <span class="text-gradient">Cơ sở hạ tầng Đám mây</span>
                    </h2>
                    <p class="banner-subheading">
                        Hệ thống CI/CD thông minh, Quản lý Kubernetes & Cơ sở hạ tầng Đa đám mây tự động.
                    </p>
                    <div class="tech-icons">
                        <i class="devicon-amazonwebservices-plain-wordmark colored" title="AWS"></i>
                        <i class="devicon-azure-plain colored" title="Azure"></i>
                        <i class="devicon-googlecloud-plain colored" title="GCP"></i>
                        <i class="devicon-kubernetes-plain colored" title="Kubernetes"></i>
                        <i class="devicon-docker-plain colored" title="Docker"></i>
                    </div>
                </div>
                <div class="banner-action">
                    <a href="#" class="cta-button">
                        Bắt Đầu Ngay ➔
                    </a>
                    <div class="cta-subtext">
                        Bảo mật Nâng cao • Tự động hóa 24/7
                    </div>
                </div>
            </div>
        </div>
"""

# Replace the existing banner section
# Find the start of the banner section
start_marker = "<!-- Main Section: Banner (Full Width) -->"
end_marker = "<!-- Service Icons (Clean Strip) -->"

start_idx = content.find(start_marker)
end_idx = content.find(end_marker)

if start_idx != -1 and end_idx != -1:
    new_content = content[:start_idx] + new_banner + "\n        " + content[end_idx:]
    with open('src/main/resources/templates/web/index.html', 'w') as f:
        f.write(new_content)
    print("Banner replaced successfully.")
else:
    print("Could not find banner markers.")
