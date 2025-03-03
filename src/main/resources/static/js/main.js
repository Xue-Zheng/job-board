// 添加自动关闭警告框的脚本
document.addEventListener('DOMContentLoaded', function() {
  // 自动关闭警告框
  const alerts = document.querySelectorAll('.alert');
  alerts.forEach(function(alert) {
    setTimeout(function() {
      const closeButton = alert.querySelector('.btn-close');
      if (closeButton) {
        closeButton.click();
      }
    }, 5000); // 5秒后自动关闭
  });

  // 添加职位描述文本编辑器增强（如果存在）
  const descriptionTextarea = document.getElementById('description');
  if (descriptionTextarea) {
    // 这里可以添加一些简单的文本编辑器增强
    // 例如自动调整高度等
    descriptionTextarea.addEventListener('input', function() {
      this.style.height = 'auto';
      this.style.height = (this.scrollHeight) + 'px';
    });
  }
});
