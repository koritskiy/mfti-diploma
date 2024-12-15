<template>
    <div v-if="organizations !== null">
        <div v-if="organizations.length > 0">
            <h2 class="mt-6 text-3xl font-bold text-primary-content text-center"> Список всех организаций </h2>

            <div class="lg:flex lg:flex-wrap lg:justify-center">
                <organization-card
                        v-for="organization in organizations"
                        :organization="organization"
                        :key="organization.id"
                />
            </div>
        </div>
    </div>

    <h2 v-else class="mt-6 text-3xl font-bold text-primary-content text-center"> Организаций пока нет </h2>
</template>

<script>
import OrganizationCard from "@/components/OrganizationCard.vue";

export default {
    components: {OrganizationCard},
    props: {
        organizations: {
            type: Array,
            required: true
        }
    }
}
</script>

<style scoped>

</style>