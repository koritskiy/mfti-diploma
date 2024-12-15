<template>
    <div class="flex items-center justify-center w-full">
        <div class="card bg-base-200 shadow-xl lg:w-1/2">
            <div class='py-12 px-10'>
                <div class="grid md:grid-cols-2 grid-cols-1">
                    <div>
                        <h1 class="text-2xl mt-8 font-bold">Task Tracker рад Вам!</h1>
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
                                    label="Ваше имя"
                                    inputType="text"
                                    v-model:model-value="user.firstName"
                                    placeholder="Например, Иван"
                            />

                            <text-input
                                    label="Ваша фамилия"
                                    inputType="text"
                                    v-model:model-value="user.lastName"
                                    placeholder="Например, Пупкин"
                            />

                            <text-input
                                    label="Ваш пароль"
                                    v-model:model-value="user.password"
                                    input-type="password"
                                    placeholder="Введите Ваш пароль"
                            />
                        </div>

                        <div class="text-center">
                            <success-outline-button @click="signup" class="w-full">Создать аккаунт
                            </success-outline-button>
                        </div>

                        <div class='text-center mt-4'>Уже есть аккаунт?
                            <span
                                    class="inline-block  hover:text-primary hover:underline hover:cursor-pointer transition duration-200"
                                    @click="this.$router.push('/login')"
                            >
              Войти!
            </span>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import TextInput from "@/components/ui/TextInput.vue";
import SuccessOutlineButton from "@/components/ui/SuccessOutlineButton.vue";

export default {
    components: {SuccessOutlineButton, TextInput},
    data() {
        return {
            user: {
                email: '',
                firstName: '',
                lastName: '',
                password: ''
            },

        }
    },
    methods: {
        signup() {
            this.$emit('signup', this.user)
        }
    }
}
</script>

<style lang="scss" scoped>

</style>