<template>
    <div class="flex flex-col items-center">
        <success-alert
                class="w-1/2"
                :is-success="isAuth"
        >Вы успешно вошли в аккаунт!
        </success-alert>
        <error-alert
                class="w-1/2"
                :is-error="isError"
        > {{ errorDescription }}
        </error-alert>
    </div>
    <div class="flex justify-center m-10 lg:ml-0 lg:mr-0">
        <login-form @login="loginUser"/>
    </div>
</template>

<script>
import LoginForm from "@/components/LoginForm.vue";
import axios from "axios";
import SuccessAlert from "@/components/ui/SuccessAlert.vue";
import ErrorAlert from "@/components/ui/ErrorAlert.vue";
import Constants from "@/components/constants";

export default {
    components: {ErrorAlert, SuccessAlert, LoginForm},
    data() {
        return {
            isAuth: false,
            isError: false,
            errorDescription: ''
        }
    },
    methods: {
        async loginUser(user) {
            await axios.post('http://localhost:8080/api/auth/v1/login/email', {
                email: user.email,
                password: user.password
            }).then((response) => {
                this.$store.commit('setAuth', 'true');
                const user = {
                    id: response.data.payload.id,
                    firstName: response.data.payload.firstName,
                    lastName: response.data.payload.lastName,
                    isUserAdmin: response.data.payload.isUserAdmin,
                    gitHubToken: response.data.payload.gitHubToken
                }
                this.$store.commit('setUser', user);
                this.$store.commit('setToken', response.data.payload.token)

                if (user.gitHubToken !== null) {
                    this.$store.commit('setGitHubToken', user.gitHubToken);
                }

                this.isAuth = true;

                setTimeout(() => this.$router.push('/account'), 1000)
                console.log(response.data.payload)
            }).catch((error) => {
                this.isError = true;
                if (error.response.data.description != null) {
                    this.errorDescription = error.response.data.description
                } else {
                    this.errorDescription = Constants.ERROR_SERVER_DESCRIPTION;
                }
            });
        }
    }
}
</script>

<style lang="scss" scoped>

</style>