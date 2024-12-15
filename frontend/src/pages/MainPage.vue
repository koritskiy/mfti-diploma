<template>
    <div class="m-10">
        <div class="lg:flex lg:flex-row lg:space-x-7 lg:w-full">
            <div class="lg:basis-1/2">
                <main-header>Добро пожаловать в Teamsync - он подойдет <span id="typing_el"></span></main-header>
                <p class="text-xl text-base-0 pt-10" id="main_text">
                    Task Tracker - это простой инструмент для управления работы с самозанятыми сотрудниками компании.
                </p>
            </div>

            <div class="pt-5 lg:pt-0 lg:w-full lg:basis-1/2">
                <main-mockup-window/>
            </div>
        </div>

        <div class="flex flex-col w-full lg:flex-row mt-10 justify-items-center gap-5">
            <div class="card bg-base-300 shadow-xl lg:basis-1/3">
                <div class="card-body">
                    <div class="avatar justify-center">
                        <div class="w-24 rounded">
                            <img :src="plus" alt="Иконка плюса" />
                        </div>
                    </div>
                    <h2 class="card-title font-bold text-2xl justify-center">
                        Зарегистрируйтесь на сайте
                    </h2>
                    <p class="text-center">
                        После регистрации на сайте Вы сможете создать страницу своей компании
                    </p>
                    <div class="card-actions justify-center mt-5">
                        <button class="btn btn-secondary gap-2" @click="this.$router.push('/signup')">
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
                                <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 9V5.25A2.25 2.25 0 0013.5 3h-6a2.25 2.25 0 00-2.25 2.25v13.5A2.25 2.25 0 007.5 21h6a2.25 2.25 0 002.25-2.25V15M12 9l-3 3m0 0l3 3m-3-3h12.75" />
                            </svg>
                            Познакомиться
                        </button>
                    </div>
                </div>
            </div>

            <div class="card bg-base-300 shadow-xl lg:basis-1/3">
                <div class="card-body">
                    <div class="avatar justify-center">
                        <div class="w-24 rounded">
                            <img :src="info" alt="Иконка информации" />
                        </div>
                    </div>
                    <h2 class="card-title font-bold text-2xl justify-center">
                        Добавьте свою компанию
                    </h2>
                    <p class="text-center">
                        Как только Вы расскажете нам о Вашей компании - откроется весь основной функционал
                    </p>
                    <div class="card-actions justify-center mt-5">
                        <button class="btn btn-secondary gap-2" @click="this.$router.push('/create-organization')">
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
                                <path stroke-linecap="round" stroke-linejoin="round" d="M12 9v6m3-3H9m12 0a9 9 0 11-18 0 9 9 0 0118 0z" />
                            </svg>
                            Рассказать о компании
                        </button>
                    </div>
                </div>
            </div>

            <div class="card bg-base-300 shadow-xl lg:basis-1/3">
                <div class="card-body">
                    <div class="avatar justify-center">
                        <div class="w-24 rounded">
                            <img :src="clock" alt="Иконка часов" />
                        </div>
                    </div>
                    <h2 class="card-title font-bold text-2xl justify-center">
                        Управляйте и наслаждайтесь
                    </h2>
                    <p class="text-center">
                        Приглашайте сотрудников, заводите задачи, управляйте ставками и многое другое - это Teamsync
                    </p>
                    <div class="card-actions justify-center mt-5">
                        <button class="btn btn-secondary gap-2" @click="this.$router.push('/create-organization')">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" /></svg>
                            Хочу попробовать
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <div class="flex flex-col w-full lg:flex-row mt-10 justify-items-center gap-5">
            <div class="card lg:card-side bg-base-200 shadow-xl w-full">
                <figure class="lg:w-96 w-48 lg:p-10 m-auto mt-10"><img :src="rocket" alt="Movie"/></figure>
                <div class="card-body">
                    <h2 class="card-title font-bold text-2xl">Используйте все возможности Teamsync</h2>
                    <p class="mt-5">
                        ✓ Управляйте своим бизнесом
                    </p>
                    <p>
                        ✓ Смотрите статистику по каждому сотруднику компании
                    </p>
                    <p>
                        ✓ Следите за выполнением всех задач
                    </p>
                    <p>
                        ✓ Выгружайте квитанции для выплаты зарплаты
                    </p>
                    <p>
                        ✓ Управляйте наймом сотрудников
                    </p>
                    <p>
                        ✓ Подключайтесь к процессам компании, не выходя из Телеграмма
                    </p>
                </div>
            </div>
        </div>

    </div>

</template>

<script>
import MainHeader from "@/components/ui/MainHeader.vue";
import MainMockupWindow from "@/components/ui/MainMockupWindow.vue";
import Typed from "typed.js";
import plus from "/src/static/plus.png";
import info from "/src/static/about.png";
import clock from "/src/static/clock.png";
import rocket from "/src/static/rocket.png";

export default {
    components: {MainMockupWindow, MainHeader},
    data() {
        return {
            plus: plus,
            info: info,
            clock: clock,
            rocket: rocket
        }
    },
    methods: {
        typingMainTitle() {
            new Typed('#typing_el', {
                strings: ["бизнесменам", "разработчикам", "рекрутерам", "каждому"],
                typeSpeed: 50
            });
        }
    },
    mounted() {
        this.$store.commit('setPageName', 'main-page');
        this.typingMainTitle();
    }
}
</script>

<style scoped>

</style>