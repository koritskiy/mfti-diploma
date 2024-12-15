<template>
    <div class="flex flex-col items-center">
        <success-alert
                class="w-1/2"
                :is-success="isAuth"
        >Вы успешно зарегистрировались!
        </success-alert>
        <error-alert
                class="w-1/2"
                :is-error="isError"
        > {{ errorDescription }}
        </error-alert>
    </div>
    <div class="flex justify-center m-10 lg:ml-0 lg:mr-0">
        <registration-form @signup="signUpUser"/>
    </div>
</template>

<script>
import RegistrationForm from "@/components/RegistrationForm.vue";
import axios from "axios";
import Constants from "@/components/constants";
import ErrorAlert from "@/components/ui/ErrorAlert.vue";
import SuccessAlert from "@/components/ui/SuccessAlert.vue";

export default {
    components: {SuccessAlert, ErrorAlert, RegistrationForm},
    data() {
        return {
            isAuth: false,
            isError: false,
            errorDescription: ''
        }
    },
    methods: {
        async signUpUser(user) {
            await axios.post('http://localhost:8080/api/auth/v1/signup', {
                email: user.email,
                firstName: user.firstName,
                lastName: user.lastName,
                password: user.password
            }).then((response) => {
                const user = {
                    id: response.data.payload.id,
                    firstName: response.data.payload.firstName,
                    lastName: response.data.payload.lastName
                }

                this.$store.commit('setAuth', 'true');
                this.$store.commit(
                    'setUser',
                    user
                );
                this.$store.commit('setToken', response.data.payload.token)

                this.isAuth = true;

                setTimeout(() => this.$router.push('/account'), 1000)
            }).catch((error) => {
                this.isError = true;
                if (error.response.data.description != null) {
                    this.errorDescription = error.response.data.description
                } else {
                    this.errorDescription = Constants.ERROR_SERVER_DESCRIPTION
                }
            })
        }
    }
}
</script>

<style lang="scss" scoped>

</style>