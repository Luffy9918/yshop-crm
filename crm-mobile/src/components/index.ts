/**
 * 公共组件统一导出
 */
import AppNavbar from './AppNavbar/AppNavbar.vue'
import CustomerCard, { type Customer } from './CustomerCard/CustomerCard.vue'
import StatusTag from './StatusTag/StatusTag.vue'
import EmptyState from './EmptyState/EmptyState.vue'
import LoadingSpinner from './LoadingSpinner/LoadingSpinner.vue'
import TabBar, { type TabItem } from './TabBar/TabBar.vue'
import CallingDialog from './CallingDialog/CallingDialog.vue'

export { AppNavbar, CustomerCard, StatusTag, EmptyState, LoadingSpinner, TabBar, CallingDialog }
export type { Customer, TabItem }
