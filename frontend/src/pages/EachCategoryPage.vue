<template>
    <div class="flex justify-center m-10">
        <div class="card bg-base-200 shadow-xl">
            <div class="card-body">
                <div class="badge badge-primary font-bold">Название категории</div>
                <h2 class="card-title text-3xl">
                    Категория {{ this.category.name }}
                </h2>
                <p>
                    На этой странице Вы можете посмотреть все задачи по отдельной категории.
                    Каждую задачу можно изменить, удалить или поменять категорию.
                </p>
            </div>
        </div>
    </div>

    <div class="flex justify-center text-sm breadcrumbs">
        <ul>
            <li><a @click="$router.push('/organizations/' + this.$route.params.id)">Компания</a></li>
            <li>Найм сотрудников</li>
            <li><a @click="$router.push('/organizations/' + this.$route.params.id + '/hiring/tasks')">База задач</a></li>
            <li>{{ this.category.name }}</li>
        </ul>
    </div>

    <div class="grid lg:grid-cols-3 m-10 gap-4">
        <div
                class="card bg-base-200 shadow-xl"
                v-for="task in category.interviewTasks"
                :key="task.id"
        >
            <div class="card-body">
                <h2 class="card-title">{{ task.name }}</h2>
                <p>
                    {{ task.question }}
                </p>
                <div class="card-actions justify-end mt-5">
                    <button class="btn btn-accent btn-sm">Изменить</button>
                    <button class="btn btn-error btn-sm">Удалить</button>
                </div>
            </div>
        </div>

    </div>
</template>

<script>
import axios from "axios";
import store from "@/store";

export default {
    data() {
        return {
            category: ''
        }
    },
    methods: {
        async getCategory() {
            await axios.get("http://localhost:8080/api/hiring/v1/taskCategory/" + this.$route.params.category_id, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload);
                this.category = response.data.payload;
            }).catch((response) => {
                console.log(response)
            })
        }
    },
    mounted() {
        this.getCategory();
    }
}
</script>