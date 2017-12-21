	.data

xin:		.hword 1
		.hword 13
		.hword 5
		.hword 45
		.hword 24
		.hword 9
		.hword 12
		.hword 24
		.hword 1
		.hword 13
		.hword 5
		.hword 45
		.hword 24
		.hword 9
		.hword 12
		.hword 24
		.hword 1
		.hword 13
		.hword 5
		.hword 45
		.hword 24
		.hword 9
		.hword 12
		.hword 24
		.hword 1
		.hword 13
		.hword 5
		.hword 45
		.hword 24
		.hword 9
		.hword 12
		.hword 24
		.hword 1
		.hword 13
		.hword 5
		.hword 45
		.hword 24
		.hword 9
		.hword 12
		.hword 24
		.hword 1
		.hword 13
		.hword 5
		.hword 45
		.hword 24
		.hword 9
		.hword 12
		.hword 24
		.hword 1
		.hword 13
		.hword 5
		.hword 45
		.hword 24
		.hword 9
		.hword 12
		.hword 24
		.hword 1
		.hword 13
		.hword 5
		.hword 45
		.hword 24
		.hword 9
		.hword 12
		.hword 24
		

xout:		.hword 10
		.hword 11
		.hword 128
		.hword 256
		.hword 512
		.hword 1024
		.hword 2048
		.hword 4096

	.equ FXP_C0, 46341
	.equ FXP_C1, 32138
	.equ FXP_C2, 30274
	.equ FXP_C3, 27245
	.equ FXP_C4, 23170
	.equ FXP_C5, 18205
	.equ FXP_C6, 12540
	.equ FXP_C7, 6393


	.global main
	.text






main:	movia r2,196752
	movia r4,xout
# Write your code there

ldh r3, 0(r2)
loop1: ldh r3,0(r2)
ldh r5,2(r2)
ldh r6,4(r2)
ldh r7,6(r2)
ldh r8,8(r2)
ldh r9,10(r2)
ldh r10,12(r2)
ldh r11,14(r2)

add r12,r3,r11
add r13,r5,r10
add r14,r6,r9
add r15,r7,r8
sub r16,r7,r8
sub r17,r6,r9
sub r18,r5,r10
sub r19,r3,r11

add r3,r12,r15
add r5,r13,r14
sub r6,r13,r14
sub r7,r12,r15
addi r8,r16,0
muli r20,r17,FXP_C4
muli r21,r18,FXP_C4
sub r9,r21,r20
srai r9,r9,15
add r10,r21,r20
srai r10,r10,15
addi r11,r19,0

muli r20,r3,FXP_C4
muli r21,r5,FXP_C4
add r12,r20,r21
srai r12,r12,15
sub r13,r20,r21
srai r13,r13,15
muli r20,r6,FXP_C6
muli r21,r7,FXP_C2
add r14,r20,r21
srai r14,r14,15
muli r20,r6,FXP_C2
muli r21,r7,FXP_C6
sub r15,r21,r20
srai r15,r15,15
add r16,r8,r9
sub r17,r8,r9
sub r18,r11,r10
add r19,r11,r10

srai r3,r12,1
srai r8,r13,1
srai r6,r14,1
srai r10,r15,1
muli r20,r16,FXP_C7
muli r21,r19,FXP_C1
add r5,r20,r21
srai r5,r5,16
muli r20,r17,FXP_C3
muli r21,r18,FXP_C5
add r9,r20,r21
srai r9,r9,16
muli r20,r17,FXP_C5
muli r21,r18,FXP_C3
sub r7,r21,r20
srai r7,r7,16
muli r20,r16,FXP_C1
muli r21,r19,FXP_C7
sub r11,r21,r20
srai r11,r11,16

sth r3,0(r2)
sth r5,2(r2)
sth r6,4(r2)
sth r7,6(r2)
sth r8,8(r2)
sth r9,10(r2)
sth r10,12(r2)
sth r11,14(r2)
addi r2,r2,16
subi r22,r22,1
beq r22,zero,8
br loop1


movia r2,xin
addi r22,zero,7
loop2: ldh r3,0(r2)
ldh r5,16(r2)
ldh r6,32(r2)
ldh r7,48(r2)
ldh r8,64(r2)
ldh r9,80(r2)
ldh r10,96(r2)
ldh r11,112(r2)

add r12,r3,r11
add r13,r5,r10
add r14,r6,r9
add r15,r7,r8
sub r16,r7,r8
sub r17,r6,r9
sub r18,r5,r10
sub r19,r3,r11

add r3,r12,r15
add r5,r13,r14
sub r6,r13,r14
sub r7,r12,r15
addi r8,r16,0
muli r20,r17,FXP_C4
muli r21,r18,FXP_C4
sub r9,r21,r20
srai r9,r9,15
add r10,r21,r20
srai r10,r10,15
addi r11,r19,0

muli r20,r3,FXP_C4
muli r21,r5,FXP_C4
add r12,r20,r21
srai r12,r12,15
sub r13,r20,r21
srai r13,r13,15
muli r20,r6,FXP_C6
muli r21,r7,FXP_C2
add r14,r20,r21
srai r14,r14,15
muli r20,r6,FXP_C2
muli r21,r7,FXP_C6
sub r15,r21,r20
srai r15,r15,15
add r16,r8,r9
sub r17,r8,r9
sub r18,r11,r10
add r19,r11,r10

srai r3,r12,1
srai r8,r13,1
srai r6,r14,1
srai r10,r15,1
muli r20,r16,FXP_C7
muli r21,r19,FXP_C1
add r5,r20,r21
srai r5,r5,16
muli r20,r17,FXP_C3
muli r21,r18,FXP_C5
add r9,r20,r21
srai r9,r9,16
muli r20,r17,FXP_C5
muli r21,r18,FXP_C3
sub r7,r21,r20
srai r7,r7,16
muli r20,r16,FXP_C1
muli r21,r19,FXP_C7
sub r11,r21,r20
srai r11,r11,16

sth r3,0(r2)
sth r5,16(r2)
sth r6,32(r2)
sth r7,48(r2)
sth r8,64(r2)
sth r9,80(r2)
sth r10,96(r2)
sth r11,112(r2)
addi r2,r2,2
subi r22,r22,1
beq r22,zero,8
br loop2


end:
	trap 0 #Infinite loop
	
	

	
	