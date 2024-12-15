<template>
    <div class="card lg:w-1/4 bg-base-200 shadow-xl m-10">
        <div class="card-body">
            <span
                    class="badge badge-success gap-2 font-bold"
                    v-if="organization.ownerUserId === store.state.user.id"
            >
                Вы владелец
            </span>
            <span
                class="badge badge-success gap-2 font-bold"
                v-else
            >
                Вы сотрудник
            </span>
            <h2 class="card-title">
                {{ organization.name }}
            </h2>
            <p>{{ organization.address }}</p>
            <div class="card-actions justify-end">
                <button class="btn btn-primary" @click="$router.push(`/organizations/${organization.id}`)">Подробнее
                </button>
                <!--        <button class="btn btn-ghost">Сайт организации</button>-->
            </div>
        </div>
    </div>
</template>

<script>
import store from "@/store";

export default {
    computed: {
        store() {
            return store
        }
    },
    props: {
        organization: {
            type: Object,
            required: true
        }
    }
}
</script>

<style lang="scss" scoped>

</style>