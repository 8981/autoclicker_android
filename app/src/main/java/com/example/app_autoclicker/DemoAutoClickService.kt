package com.example.app_autoclicker

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class DemoAutoClickService : AccessibilityService() {

    private var lastClickTime = 0L

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        // Обрабатываем только наше приложение
        if (event.packageName != packageName) return

        val root = rootInActiveWindow ?: return

        val now = System.currentTimeMillis()
        if (now - lastClickTime < 500) return

        // Ищем кнопку по ID
        val buttonNodes = root.findAccessibilityNodeInfosByViewId(
            "$packageName:id/btnTake"
        )

        if (buttonNodes.isNullOrEmpty()) return

        val buttonNode = buttonNodes[0]
        val clicked = buttonNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)

        if (clicked) {
            lastClickTime = now
            Log.d("DemoAutoClickService", "Кнопка нажата службой")
        }
    }

    override fun onInterrupt() {
        // ничего
    }
}
