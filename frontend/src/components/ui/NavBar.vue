<template>
    <div class="navbar bg-base-100">
        <div class="navbar-start">
            <div class="dropdown">
                <label tabindex="0" class="btn btn-ghost lg:hidden">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24"
                         stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M4 6h16M4 12h8m-8 6h16"/>
                    </svg>
                </label>
                <ul tabindex="0"
                    class="menu menu-compact dropdown-content mt-3 p-2 shadow bg-base-100 rounded-box w-52">
                    <!--     МОБИЛЬНОЕ МЕНЮ     -->
                    <li>
                        <a
                                @click="$router.push('/')"
                                :class="{'active' : pageName === 'main-page'}"
                        >
                            Главная
                        </a>
                    </li>
                    <li>
                        <a
                                @click="$router.push('/organizations')"
                                :class="{'active' : pageName === 'all-organizations-page'}"
                        >
                            Все организации
                        </a>
                    </li>
                </ul>
            </div>
            <a @click="$router.push('/')" class="btn btn-ghost normal-case text-xl">Teamsync</a>
        </div>
        <div class="navbar-center hidden lg:flex lg:justify-center">
            <ul class="menu menu-horizontal px-1">
                <!--     ОБЫЧНОЕ МЕНЮ     -->
                <li>
                    <a
                            @click="$router.push('/')"
                            :class="{'active' : pageName === 'main-page'}"
                    >
                        Главная
                    </a>
                </li>
                <li>
                    <a
                            @click="$router.push('/organizations')"
                            :class="{'active' : pageName === 'all-organizations-page'}"
                    >
                        Все организации
                    </a>
                </li>
            </ul>
        </div>
        <div class="navbar-end invisible lg:visible">
            <a
                    v-if="isAuth === false"
                    class="btn btn-outline btn-success"
                    @click="$router.push('/login')"
            >Войти</a>

            <div v-else>
                <div class="dropdown dropdown-end">
                    <label tabindex="0" class="btn btn-ghost btn-circle avatar">
                        <div class="w-10 rounded-full ring ring-success ring-offset-base-200 ring-offset-2">
                            <img :src="defaultAvatar" alt="Аватар по умолчанию"/>
                        </div>
                    </label>
                    <ul tabindex="0"
                        class="menu menu-compact dropdown-content mt-3 p-2 shadow bg-base-200 rounded-box w-52 z-[1]">
                        <li>
                            <a
                                    @click="this.$router.push('/account')"
                                    class="justify-between"
                                    :class="{'active' : pageName === 'account'}">
                                Профиль
                            </a>
                        </li>
                        <li>
                            <a
                                    @click="this.$router.push('/account/settings')"
                                    class="justify-between"
                                    :class="{'active' : pageName === 'account-settings'}"
                            >
                                Настройки
                            </a>
                        </li>
                        <li><a @click="logout">Выйти</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import {mapState} from "vuex";
import defaultAvatar from "/src/static/default-avatar-2.png"

export default {
    name: 'NavBar',
    computed: {
        ...mapState({
            isAuth: state => state.isAuth,
            pageName: state => state.pageName
        })
    },
    methods: {
        logout() {
            this.$router
                .push('/')
                .then(() => sessionStorage.clear())
                .then(() => this.$router.go());
        }
    },
    data() {
        return {
            defaultAvatar: defaultAvatar
        }
    }
}
</script>

<style lang="scss" scoped>

</style>