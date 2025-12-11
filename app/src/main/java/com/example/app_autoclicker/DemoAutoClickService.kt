package com.example.app_autoclicker  // <-- оставь свой package

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class DemoAutoClickService : AccessibilityService() {

    private val TAG = "DemoAutoClickService"
    private var lastClickTime = 0L

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "onServiceConnected: сервис доступности запущен")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        Log.d(
            TAG,
            "onAccessibilityEvent: type=${event.eventType}, package=${event.packageName}"
        )

        // Пока НЕ фильтруем по packageName, чтобы увидеть все события
        val root = rootInActiveWindow
        if (root == null) {
            Log.d(TAG, "rootInActiveWindow == null")
            return
        }

        val now = System.currentTimeMillis()
        if (now - lastClickTime < 500) {
            Log.d(TAG, "слишком часто, пропускаю клик")
            return
        }

        // 1) Пытаемся найти по ID
        val id = "$packageName:id/btnTake"
        val nodesById: List<AccessibilityNodeInfo> =
            try {
                root.findAccessibilityNodeInfosByViewId(id)
            } catch (e: Exception) {
                Log.d(TAG, "Ошибка в findAccessibilityNodeInfosByViewId: ${e.message}")
                emptyList()
            }

        Log.d(TAG, "nodesById size = ${nodesById.size} (id = $id)")

        var targetNode: AccessibilityNodeInfo? = null

        if (nodesById.isNotEmpty()) {
            targetNode = nodesById[0]
        } else {
            // 2) Если по ID не нашли – пробуем по тексту кнопки
            val nodesByText = root.findAccessibilityNodeInfosByText("Взять заказ")
            Log.d(TAG, "nodesByText size = ${nodesByText.size}")
            if (nodesByText.isNotEmpty()) {
                targetNode = nodesByText[0]
            }
        }

        if (targetNode == null) {
            Log.d(TAG, "Не нашли кнопку ни по ID, ни по тексту")
            return
        }

        val clicked = targetNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        Log.d(TAG, "performAction(ACTION_CLICK) result = $clicked")

        if (clicked) {
            lastClickTime = now
        }
    }

    override fun onInterrupt() {
        Log.d(TAG, "onInterrupt")
    }
}
