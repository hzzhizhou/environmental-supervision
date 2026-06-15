import { 
    DataAnalysis, 
    Checked, 
    Document, 
    TrendCharts, 
    PieChart, 
    DataLine, 
    Files, 
    User, 
    Grid 
} from '@element-plus/icons-vue'

export const menuList = [
    {
        title: '公众监督数据管理',
        children: [
            { name: '公众监督数据列表', path: '/data-list', icon: DataAnalysis, badge: 23 },
            { name: '确认AQI数据列表', path: '/confirm-list', icon: Checked, badge: 15 },
            { name: '数据审核记录', path: '/audit-log', icon: Document },
        ]
    },
    {
        title: '统计数据管理',
        children: [
            { name: '省分组检查统计', path: '/province-stat', icon: TrendCharts },
            { name: 'AQI指数分布统计', path: '/distribution', icon: PieChart },
            { name: 'AQI指数趋势统计', path: '/trend', icon: DataLine },
            { name: '其它数据统计', path: '/other-stat', icon: Files },
        ]
    },
    {
        title: '系统管理',
        children: [
            { name: '用户权限管理', path: '/user-manage', icon: User },
            { name: '网格区域配置', path: '/grid-config', icon: Grid },
        ]
    }
]