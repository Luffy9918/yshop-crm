const { chromium } = require('playwright');

(async () => {
  const browser = await chromium.launch({
    headless: false,
    slowMo: 500
  });
  const page = await browser.newPage();

  console.log('正在打开 yshop-crm 前端页面...');
  await page.goto('http://localhost:80');

  // 等待页面加载
  await page.waitForTimeout(3000);

  // 截图
  await page.screenshot({ path: 'D:\\crm\\yshop-crm\\screenshot-login.png', fullPage: true });
  console.log('登录页面截图已保存');

  // 查看页面快照
  const snapshot = await page.accessibility.snapshot();
  console.log('页面快照:', JSON.stringify(snapshot, null, 2).substring(0, 500));

  // 保持浏览器打开以便用户交互
  console.log('浏览器已打开，按 Ctrl+C 退出...');

  // 不自动关闭浏览器，让用户可以交互
  // await browser.close();
})();
