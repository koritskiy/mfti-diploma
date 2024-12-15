<template>
    <div class="flex items-center justify-center w-full">
        <div class="card bg-base-200 shadow-xl lg:w-1/2">
            <div class='py-12 px-10'>
                <div class="grid md:grid-cols-2 grid-cols-1">
                    <div class="lg:ml-5 lg:mr-5">
                        <h1 class="text-2xl mt-8 font-bold">Task Tracker рад Вашему возвращению!</h1>
                        <p class="py-2 mt-4">
                            ✓ Управляйте своим бизнесом
                        </p>
                        <p class="py-2 ">
                            ✓ Смотрите статистику по каждому сотруднику компании
                        </p>
                        <p class="py-2  ">
                            ✓ Следите за выполнением всех задач
                        </p>
                    </div>
                    <form @submit.prevent method="post">

                        <div class="mb-4">
                            <text-input
                                    label="Ваш Email"
                                    inputType="email"
                                    v-model:model-value="user.email"
                                    placeholder="Например, example@tracker.com"
                            />

                            <text-input
                                    label="Ваш пароль"
                                    v-model:model-value="user.password"
                                    input-type="password"
                                    placeholder="Введите Ваш пароль"
                            />
                        </div>

                        <div class='text-right font-bold text-primary'>
                            <a
                                    @click="this.$router.push('/forgot/password')"
                                    class="text-sm inline-block hover:text-primary hover:underline hover:cursor-pointer transition duration-200">
                                Забыли пароль?
                            </a>
                        </div>

                        <div class="text-center">
                            <success-outline-button @click="login" class="">Войти</success-outline-button>
                        </div>

                        <div class='text-center mt-4'>Еще нет аккаунта?
                            <span
                                    class="inline-block hover:text-primary hover:underline hover:cursor-pointer transition duration-200"
                                    @click="this.$router.push('/signup')"
                            >
                Зарегистрироваться!
              </span>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import SuccessOutlineButton from "@/components/ui/SuccessOutlineButton.vue";
import TextInput from "@/components/ui/TextInput.vue";

export default {
    components: {SuccessOutlineButton, TextInput},
    data() {
        return {
            user: {
                email: '',
                password: ''
            },

        }
    },
    methods: {
        login() {
            this.$emit('login', this.user)
        }
    }
}
</script>

<style lang="scss" scoped>

</style>