import {createRouter, createWebHistory} from 'vue-router'
import StartLoginPage from "@/pages/login/StartLoginPage.vue";
import FinishLoginPage from "@/pages/login/FinishLoginPage.vue";
import StartSignUpPage from "@/pages/signup/StartSignUpPage.vue";
import FinishSignUpPage from "@/pages/signup/FinishSignUpPage.vue";
import AccountPage from "@/pages/account/AccountPage.vue";

const routes = [
    {
        path: '/',
        name: 'start_login_page',
        component: StartLoginPage
    },
    {
        path: '/login/finish',
        name: 'finish_login_page',
        component: FinishLoginPage,
        props: router => ({email: router.query.email})
    },
    {
        path: '/signup/start',
        name: 'start_signup_page',
        component: StartSignUpPage
    },
    {
        path: '/signup/finish',
        name: 'finish_signup_page',
        component: FinishSignUpPage,
        props: router => ({
            email: router.query.email,
            firstName: router.query.firstName,
            lastName: router.query.lastName
        })
    },
    {
        path: '/account',
        name: 'account_page',
        component: AccountPage
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router
