<template>
    <div>
        <div class="min-h-screen">
            <nav-bar/>
            <router-view/>
            <footer-element></footer-element>
        </div>
    </div>
</template>

<script setup>
import NavBar from "@/components/ui/NavBar.vue";
import FooterElement from "@/components/ui/FooterElement.vue";
</script>

<style>

</style>