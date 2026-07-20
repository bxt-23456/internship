// Vue2 Header组件
Vue.component('header-component', {
    template: `
        <div class="header">
            <div class="header-content">
                <div class="logo">
                    <span>健康之路在线医疗</span>
                </div>
                <div class="nav">
                    <a href="index.html" :class="{active: activeNav === 'home'}">首页</a>
                    <a href="hospital_with_api.html" :class="{active: activeNav === 'hospital'}">找医院</a>
                    <a href="doctor.html" :class="{active: activeNav === 'doctor'}">找医生</a>
                    <a href="disease.html" :class="{active: activeNav === 'disease'}">查疾病</a>
                    <a href="article.html" :class="{active: activeNav === 'article'}">健康科普</a>
                </div>
                <div class="search-box">
                    <input type="text" v-model="searchText" placeholder="搜索医院、医生、疾病..." @keyup.enter="handleSearch">
                    <button @click="handleSearch">搜索</button>
                </div>
                <div class="auth">
                    <template v-if="isLoggedIn">
                        <a href="personal-info.html" class="user-info">你好, {{ displayName }}</a>
                        <a href="javascript:;" class="logout-btn" @click="handleLogout">退出</a>
                    </template>
                    <template v-else>
                        <a href="login.html" class="login-btn">登录</a>
                        <a href="register.html" class="register-btn">注册</a>
                    </template>
                </div>
            </div>
        </div>
    `,
    props: {
        activeNav: {
            type: String,
            default: 'home'
        }
    },
    data() {
        return {
            searchText: '',
            isLoggedIn: false,
            displayName: ''
        };
    },
    mounted() {
        // 检查登录状态（从localStorage获取token和userInfo）
        const token = localStorage.getItem('token');
        const userInfo = localStorage.getItem('userInfo');
        if (token && userInfo) {
            const user = JSON.parse(userInfo);
            this.isLoggedIn = true;
            // 优先显示真实姓名，其次用户名，最后手机号
            this.displayName = user.realName || user.username || this.formatPhone(user.phone);
        }
    },
    methods: {
        formatPhone(phone) {
            if (!phone) return '';
            return phone.replace(/(\d{3})(\d{4})(\d{4})/, '$1****$3');
        },
        handleSearch() {
            if (this.searchText.trim()) {
                window.location.href = 'search.html?keyword=' + encodeURIComponent(this.searchText);
            }
        },
        handleLogout() {
            // 调用后端退出登录接口，清除Redis中的登录状态
            axios.post('/user/logout').then(() => {
                localStorage.removeItem('token');
                localStorage.removeItem('userInfo');
                this.isLoggedIn = false;
                this.displayName = '';
                window.location.href = 'index.html';
            }).catch(() => {
                // 即使后端调用失败，也清除本地状态
                localStorage.removeItem('token');
                localStorage.removeItem('userInfo');
                this.isLoggedIn = false;
                this.displayName = '';
                window.location.href = 'index.html';
            });
        }
    }
});